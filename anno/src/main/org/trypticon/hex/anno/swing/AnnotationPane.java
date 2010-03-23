/*
 * Hex - a hex viewer and annotator
 * Copyright (C) 2009  Trejkaz, Hex Project
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

package org.trypticon.hex.anno.swing;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.trypticon.hex.anno.AnnotationCollection;
import org.trypticon.hex.binary.Binary;
import org.trypticon.hex.util.swingxsupport.NullTreeTableModel;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Panel displaying the annotations.
 *
 * @author trejkaz
 */
public class AnnotationPane extends JPanel {

    private JXTreeTable annoTreeTable;
    private TreeTableModel annoTreeTableModel;

    private AnnotationCollection annotations;
    private Binary binary;

    public AnnotationPane() {
        annoTreeTable = new JXTreeTable(new NullTreeTableModel());
        setLayout(new BorderLayout());
        JScrollPane annoTreeTableScroll = new JScrollPane(annoTreeTable);
        Dimension preferredSize = annoTreeTableScroll.getPreferredSize();
        preferredSize.width = 400;
        annoTreeTableScroll.setPreferredSize(preferredSize);
        add(annoTreeTableScroll, BorderLayout.CENTER);

        // Hijack changes to the model to set appropriate column widths.  Actually there is probably a better way...
        annoTreeTable.addPropertyChangeListener("model", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent event) {
                TableColumnModel columnModel = annoTreeTable.getColumnModel();
                if (columnModel.getColumnCount() >= 3) {
                    columnModel.getColumn(AnnotationTreeTableModel.TYPE_COLUMN).setPreferredWidth(100);
                    columnModel.getColumn(AnnotationTreeTableModel.VALUE_COLUMN).setPreferredWidth(100);
                    columnModel.getColumn(AnnotationTreeTableModel.NOTE_COLUMN).setPreferredWidth(200);
                }
            }
        });
    }

    public AnnotationCollection getAnnotations() {
        if (annoTreeTableModel instanceof AnnotationTreeTableModel) {
            return ((AnnotationTreeTableModel) annoTreeTableModel).getAnnotations();
        } else {
            return null; // happens when it's the null model.
        }
    }

    public void setAnnotations(AnnotationCollection annotations) {
        this.annotations = annotations;
        createModelIfComplete();
    }

    public void setBinary(Binary binary) {
        this.binary = binary;
        createModelIfComplete();
    }

    private void createModelIfComplete() {
        if (annotations != null && binary != null) {
            annoTreeTableModel = new AnnotationTreeTableModel(annotations, binary);
        } else {
            annoTreeTableModel = new NullTreeTableModel();
        }
        annoTreeTable.setTreeTableModel(annoTreeTableModel);
    }
}