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

package org.trypticon.hex.formats.classfile.constantpool;

import org.trypticon.hex.anno.Annotation;
import org.trypticon.hex.anno.SimpleMutableGroupAnnotation;
import org.trypticon.hex.binary.Binary;
import org.trypticon.hex.formats.classfile.AbstractClassFileStructure;

import java.util.Arrays;

/**
 * Common structure for "CONSTANT_Fieldref_info", "CONSTANT_Methodref_info" and "CONSTANT_InterfaceMethodref_info"
 *
 * @author trejkaz
 */
public abstract class AbstractConstantRefInfo extends AbstractClassFileStructure {
    protected AbstractConstantRefInfo(String name) {
        super(name);
    }

    public Annotation drop(Binary binary, long position) {
        long pos = position;
        Annotation tag              = u1(pos, "tag");                 pos += 1;
        Annotation classIndex       = u2(pos, "class_index");         pos += 2;
        Annotation nameAndTypeIndex = u2(pos, "name_and_type_index"); pos += 2;

        int length = (int) (pos - position);
        return new SimpleMutableGroupAnnotation(position, length, getName(),
                                                Arrays.asList(tag, classIndex, nameAndTypeIndex));
    }
}
