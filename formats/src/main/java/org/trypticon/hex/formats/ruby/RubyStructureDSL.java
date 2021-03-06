/*
 * Hex - a hex viewer and annotator
 * Copyright (C) 2009-2014  Trejkaz, Hex Project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.trypticon.hex.formats.ruby;

import java.io.IOException;
import java.io.InputStream;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.TestOnly;
import org.jruby.embed.LocalContextScope;
import org.jruby.embed.LocalVariableBehavior;
import org.jruby.embed.ScriptingContainer;

import org.trypticon.hex.formats.Structure;
import org.trypticon.hex.interpreters.MasterInterpreterStorage;

/**
 * Java entry point to a Ruby DSL for creating structures.
 *
 * @author trejkaz
 */
public class RubyStructureDSL {
    private final boolean classpath;
    private final String scriptlet;

    private RubyStructureDSL(boolean classpath, @NonNls String scriptlet) {
        this.classpath = classpath;
        this.scriptlet = scriptlet;
    }

    @TestOnly
    public static Structure loadScriptlet(@NonNls String scriptlet) {
        return new RubyStructureDSL(false, scriptlet).createStructure();
    }

    public static Structure loadFromClasspath(@NonNls String path) {
        return new RubyStructureDSL(true, path).createStructure();
    }

    private Structure createStructure() {
        // Default behaviour of ScriptingContainer seems broken, but we can improve it.
        ScriptingContainer container = new ScriptingContainer(LocalContextScope.THREADSAFE,
                                                              LocalVariableBehavior.PERSISTENT);
        try {
            container.put("$interpreter_storage", new MasterInterpreterStorage());

            // Set up the library scripts will have by default.
            @NonNls
            String fileName = RubyStructureDSL.class.getPackage().getName().replace('.', '/') + "/structure_dsl.rb";
            try (InputStream resource = getClass().getResourceAsStream("structure_dsl.rb")) {
                container.runScriptlet(resource, "classpath:" + fileName);
            }

            // Run the script itself, which should return a Structure instance.
            Object instance;
            if (classpath) {
                try (InputStream resource = getClass().getResourceAsStream(scriptlet)) {
                    instance = container.runScriptlet(resource, "classpath:" + scriptlet);
                }
            } else {
                instance = container.runScriptlet(scriptlet);
            }

            return container.getInstance(instance, Structure.class);
        } catch (IOException | RuntimeException e) {
            throw new RuntimeException("Error loading script: \n" + scriptlet, e);
        } finally {
            container.terminate();
        }
    }
}
