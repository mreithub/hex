/*
 * Hex - a hex viewer and annotator
 * Copyright (C) 2009-2010  Trejkaz, Hex Project
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

package org.trypticon.hex.formats.jpeg;

import org.trypticon.hex.anno.Annotation;
import org.trypticon.hex.anno.SimpleMutableGroupAnnotation;
import org.trypticon.hex.binary.Binary;
import org.trypticon.hex.formats.ConvenienceStructure;
import org.trypticon.hex.interpreters.primitives.UShortInterpreterBE;

import java.util.Arrays;

/**
 * Base class for JPEG blocks which only consist of the two byte marker.
 *
 * @author trejkaz
 */
abstract class JpegEmptyBlock extends ConvenienceStructure {
    private final String name;
    private final short magic;

    protected JpegEmptyBlock(String name, short magic) {
        super("JPEG " + name);
        this.name = name;
        this.magic = magic;
    }

    public Annotation drop(Binary binary, long position) {
        Annotation blockId = magic(binary, position, new UShortInterpreterBE(), name, magic);

        return new SimpleMutableGroupAnnotation(position, 2, name, Arrays.asList(blockId));
    }
}
