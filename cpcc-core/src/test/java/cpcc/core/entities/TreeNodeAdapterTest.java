// This code is part of the CPCC-NG project.
//
// Copyright (c) 2013 Clemens Krainer <clemens.krainer@gmail.com>
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software Foundation,
// Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.

package cpcc.core.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * TreeNodeAdapterTest
 */
public class TreeNodeAdapterTest
{
    private TreeNodeAdapter treeNodeAdapter;
    private ITreeNode treeNode;

    @BeforeEach
    public void setUp()
    {
        treeNodeAdapter = new TreeNodeAdapter();
        treeNode = mock(ITreeNode.class);
    }

    @Test
    public void shouldHandleLeafCorrectly()
    {
        when(treeNode.isLeaf()).thenReturn(true);
        assertThat(treeNodeAdapter.isLeaf(treeNode)).isEqualTo(true);
        verify(treeNode).isLeaf();

        when(treeNode.isLeaf()).thenReturn(false);
        assertThat(treeNodeAdapter.isLeaf(treeNode)).isEqualTo(false);

        verify(treeNode, times(2)).isLeaf();
    }

    @Test
    public void shouldHandleHasChildrenCorrectly()
    {
        when(treeNode.hasChildren()).thenReturn(false);
        assertThat(treeNodeAdapter.hasChildren(treeNode)).isEqualTo(false);
        verify(treeNode).hasChildren();

        when(treeNode.hasChildren()).thenReturn(true);
        assertThat(treeNodeAdapter.hasChildren(treeNode)).isEqualTo(true);
        verify(treeNode, times(2)).hasChildren();
    }

    @Test
    public void shouldHandleGetChildrenCorrectly()
    {
        when(treeNode.getChildren()).thenReturn(null);
        assertThat(treeNodeAdapter.getChildren(treeNode)).isNull();

        when(treeNode.getChildren()).thenReturn(new ArrayList<ITreeNode>());
        assertThat(treeNodeAdapter.getChildren(treeNode)).isNotNull().isEmpty();

        when(treeNode.getChildren()).thenReturn(Arrays.asList(treeNode));
        assertThat(treeNodeAdapter.getChildren(treeNode)).isNotNull().hasSize(1).containsExactly(treeNode);
    }

    static Stream<Arguments> labelDataProvider()
    {
        return Stream.of(
            arguments((String) null),
            arguments(""),
            arguments("label1"),
            arguments("label1 und label2"));
    };

    @ParameterizedTest
    @MethodSource("labelDataProvider")
    public void shouldHandleGetLabelCorrectly(String label)
    {
        when(treeNode.getLabel()).thenReturn(label);
        assertThat(treeNodeAdapter.getLabel(treeNode)).isEqualTo(label);
    }

}
