package application;

import java.awt.*;

public class ColorFinder {
    // Declare a root node to contain all of the source colors.
    private Node _nodRoot;

    public ColorFinder(Color[] sourceColors) {
        // Create the root node.
        _nodRoot = new Node(0, sourceColors);
        // Add all source colors to it.
        for(int i = 0; i < sourceColors.length; i++) {
            _nodRoot.AddColor(i);
        }
    }

    public int GetNearestColorIndex(Color color) {
        int iTmp = 0;
        return _nodRoot.GetNearestColorIndex(color, iTmp);
    }

    private class Node {
        private int _iLevel;
        private Color[] _aclSourceColors;
        private Node[] _anodChildren = new Node[8];
        private int _iColorIndex = -1;

        // Cached distance calculations.
        private int[][] Distance = new int[256][256];

        // Cached bit masks.
        private int[] Mask = { 128, 64, 32, 16, 8, 4, 2, 1 };

        public Node() {
            // precalculate every possible distance
            for(int i = 0; i < 256; i++) {
                for(int j = 0; j < 256; j++) {
                    Distance[i][j] = ((i - j) * (i - j));
                }
            }
        }

        public Node(int level, Color[] sourceColors) {
            _iLevel = level;
            _aclSourceColors = sourceColors;
        }

        public void AddColor(int colorIndex) {
            if(_iLevel == 7) {
                // LEAF MODE.
                // The deepest level contains the color index and no children.
                _iColorIndex = colorIndex;
            } else {
                // BRANCH MODE.
                // Get the oct index for the specified source color at this level.
                int iIndex = _GetOctIndex(_aclSourceColors[colorIndex], _iLevel);
                // If the necessary child node doesn't exist, create it.
                if(_anodChildren[iIndex] == null) {
                    _anodChildren[iIndex] = new Node((_iLevel + 1), _aclSourceColors);
                }
                // Pass the color along to the proper child node.
                _anodChildren[iIndex].AddColor(colorIndex);
            }
        }

        public int GetNearestColorIndex(Color color, int distance) {
            int ret = -1;
            if(_iLevel == 7) {
                // LEAF MODE.
                // Return our color index.
                ret = _iColorIndex;
                // Output the distance in case the caller is comparing distances.
                distance = ( Distance[color.getRed()][_aclSourceColors[ret].getRed()] +
                        Distance[color.getGreen()][_aclSourceColors[ret].getGreen()] +
                        Distance[color.getBlue()][_aclSourceColors[ret].getBlue()] );
            } else {
                // BRANCH MODE.
                // Get the oct index for the specified color at this level.
                int iIndex = _GetOctIndex(color, _iLevel);
                if(_anodChildren[iIndex] == null) {
                    // NO DIRECT CHILD EXISTS.
                    // Return the child containing the closeset color.
                    int iMinDistance = 2147483647;
                    int iMinColor = -1;
                    for(Node nod : _anodChildren) {
                        if(nod != null) {
                            // Get the closeset color contained in the child node and its distance.
                            int iDistance_nod = 0;
                            int iColor_nod = nod.GetNearestColorIndex(color, iDistance_nod);
                            // If the return color the closest color found, remember it.
                            if(iDistance_nod < iMinDistance) {
                                iMinDistance = iDistance_nod;
                                iMinColor = iColor_nod;
                            }
                        }
                    }
                    // Return the closest color index found and output its distance.
                    ret = iMinColor;
                    distance = iMinDistance;
                } else {
                    // DIRECT CHILD EXISTS.
                    // Return its closest color and distance.
                    ret = _anodChildren[iIndex].GetNearestColorIndex(color, distance);
                }
            }
            return ret;
        }

        private int _GetOctIndex(Color color, int level) {
            // Take 1 bit from each color channel to return a 3-bit value ranging from 0 to 7.
            // Level 0 uses the highest bit, level 1 uses the second-highest bit, etc.
            int iShift = (7 - level);
            return ( ((color.getRed() & Mask[level]) >> iShift     ) |
                    ((color.getGreen() & Mask[level]) << 1 >> iShift) |
                    ((color.getBlue() & Mask[level]) << 2 >> iShift) );
        }
    }
}