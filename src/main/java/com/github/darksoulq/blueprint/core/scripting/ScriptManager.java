package com.github.darksoulq.blueprint.core.scripting;

import com.github.darksoulq.abyssallib.server.scheduler.Scheduler;
import com.github.darksoulq.blueprint.Blueprint;
import com.github.darksoulq.blueprint.core.scripting.api.ScriptUtils;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ScriptManager {

    private static final Map<String, CachedScript> SCRIPT_CACHE = new ConcurrentHashMap<>();

    private record CachedScript(long lastModified, CompiledScript compiledScript) {}

    private final ScriptEngine engine;
    private final ScriptUtils scriptUtils;
    private final String cachedClasspath;

    public ScriptManager(Scheduler scheduler) {
        this.cachedClasspath = buildPluginClasspath();
        String origVersion = System.getProperty("java.version");
        String origSpec = System.getProperty("java.specification.version");
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();

        try {
            System.setProperty("java.version", "21.0.0");
            System.setProperty("java.specification.version", "21");
            System.setProperty("idea.use.native2.idea", "true");
            System.setProperty("kotlin.script.classpath", this.cachedClasspath);

            Thread.currentThread().setContextClassLoader(Blueprint.class.getClassLoader());

            ScriptEngineManager manager = new ScriptEngineManager(Blueprint.class.getClassLoader());
            this.engine = manager.getEngineByExtension("kts");
            this.scriptUtils = new ScriptUtils(scheduler);

            if (this.engine != null) {
                Bindings bindings = this.engine.getBindings(ScriptContext.ENGINE_SCOPE);
                bindings.put("utils", scriptUtils);
            } else {
                Blueprint.getPlugin().getLogger().severe("CRITICAL: KTS ScriptEngine could not be loaded!");
            }
        } finally {
            Thread.currentThread().setContextClassLoader(oldLoader);
            if (origVersion != null) System.setProperty("java.version", origVersion);
            if (origSpec != null) System.setProperty("java.specification.version", origSpec);
        }
    }

    public Object eval(File scriptFile) {
        if (this.engine == null) {
            Blueprint.getPlugin().getLogger().severe("Cannot evaluate " + scriptFile.getName() + " because the Kotlin ScriptEngine is null.");
            return null;
        }
        if (!scriptFile.exists()) {
            Blueprint.getPlugin().getLogger().severe("Script file not found: " + scriptFile.getAbsolutePath());
            return null;
        }

        long lastMod = scriptFile.lastModified();
        String path = scriptFile.getAbsolutePath();

        CachedScript cached = SCRIPT_CACHE.get(path);
        if (cached != null && cached.lastModified() == lastMod) {
            try {
                return cached.compiledScript().eval();
            } catch (Exception e) {
                Blueprint.getPlugin().getLogger().severe("Error evaluating cached script: " + scriptFile.getName());
                e.printStackTrace();
                return null;
            }
        }

        String origVersion = System.getProperty("java.version");
        String origSpec = System.getProperty("java.specification.version");
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();

        try {
            System.setProperty("java.version", "21.0.0");
            System.setProperty("java.specification.version", "21");
            System.setProperty("kotlin.script.classpath", this.cachedClasspath);

            Thread.currentThread().setContextClassLoader(Blueprint.class.getClassLoader());

            StringBuilder imports = new StringBuilder();
            imports.append("import com.github.darksoulq.blueprint.core.scripting.api.*\n");
            imports.append("import org.bukkit.entity.*\n");
            imports.append("import org.bukkit.inventory.*\n");
            imports.append("import org.bukkit.block.*\n");
            imports.append("import org.bukkit.Material\n");
            imports.append("import com.github.darksoulq.abyssallib.world.entity.*\n");
            imports.append("import com.github.darksoulq.abyssallib.world.item.*\n");
            imports.append("import com.github.darksoulq.abyssallib.server.event.*\n");
            imports.append("import com.github.darksoulq.abyssallib.server.event.context.item.*\n");
            imports.append("import com.destroystokyo.paper.entity.ai.*\n");

            StringBuilder body = new StringBuilder();
            List<String> lines = Files.readAllLines(scriptFile.toPath());
            for (String line : lines) {
                if (line.trim().startsWith("import ")) {
                    imports.append(line).append("\n");
                } else {
                    body.append(line).append("\n");
                }
            }

            String injectedFunction = "fun goal(keyName: String, vararg types: GoalType, logic: (Mob) -> ScriptedGoalLogic): java.util.function.Function<Mob, com.destroystokyo.paper.entity.ai.Goal<Mob>> {\n" +
                "    return utils.createGoal(keyName, types.toList(), logic)\n" +
                "}\n";

            String finalCode = imports.append(injectedFunction).append(body).toString();

            if (engine instanceof Compilable compilable) {
                CompiledScript compiled = compilable.compile(finalCode);
                SCRIPT_CACHE.put(path, new CachedScript(lastMod, compiled));
                return compiled.eval();
            } else {
                return engine.eval(finalCode);
            }
        } catch (Exception e) {
            Blueprint.getPlugin().getLogger().severe("Runtime/Syntax error evaluating script: " + scriptFile.getName());
            e.printStackTrace();
            return null;
        } finally {
            Thread.currentThread().setContextClassLoader(oldLoader);
            if (origVersion != null) System.setProperty("java.version", origVersion);
            if (origSpec != null) System.setProperty("java.specification.version", origSpec);
        }
    }

    private String buildPluginClasspath() {
        StringBuilder cp = new StringBuilder();
        ClassLoader classLoader = Blueprint.class.getClassLoader();

        if (classLoader instanceof URLClassLoader urlClassLoader) {
            for (URL url : urlClassLoader.getURLs()) {
                try {
                    File file = new File(url.toURI());
                    cp.append(file.getAbsolutePath()).append(File.pathSeparator);
                } catch (Exception ignored) {}
            }
        }

        File librariesFolder = new File("libraries");
        if (librariesFolder.exists() && librariesFolder.isDirectory()) {
            scanForJars(librariesFolder, cp);
        }

        File pluginsFolder = new File("plugins");
        if (pluginsFolder.exists() && pluginsFolder.isDirectory()) {
            scanForJars(pluginsFolder, cp);
        }

        cp.append(System.getProperty("java.class.path"));
        return cp.toString();
    }

    private void scanForJars(File folder, StringBuilder cp) {
        File[] files = folder.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                scanForJars(file, cp);
            } else if (file.getName().endsWith(".jar")) {
                cp.append(file.getAbsolutePath()).append(File.pathSeparator);
            }
        }
    }
}