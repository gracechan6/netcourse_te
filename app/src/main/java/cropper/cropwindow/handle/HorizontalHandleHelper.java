/*
 * Copyright 2013, Edmodo, Inc. 
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with the License.
 * You may obtain a copy of the License in the LICENSE file, or at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" 
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language 
 * governing permissions and limitations under the License. 
 */

package cropper.cropwindow.handle;

import android.graphics.Rect;

/**
 * Handle helper class to handle horizontal handles (i.e. top and bottom
 * handles).
 */
class HorizontalHandleHelper extends cropper.cropwindow.handle.HandleHelper {

    // Member Variables ////////////////////////////////////////////////////////

    private cropper.cropwindow.edge.Edge mEdge;

    // Constructor /////////////////////////////////////////////////////////////

    HorizontalHandleHelper(cropper.cropwindow.edge.Edge edge) {
        super(edge, null);
        mEdge = edge;
    }

    // HandleHelper Methods ////////////////////////////////////////////////////

    @Override
    void updateCropWindow(float x,
                          float y,
                          float targetAspectRatio,
                          Rect imageRect,
                          float snapRadius) {

        // Adjust this Edge accordingly.
        mEdge.adjustCoordinate(x, y, imageRect, snapRadius, targetAspectRatio);

        float left = cropper.cropwindow.edge.Edge.LEFT.getCoordinate();
        float top = cropper.cropwindow.edge.Edge.TOP.getCoordinate();
        float right = cropper.cropwindow.edge.Edge.RIGHT.getCoordinate();
        float bottom = cropper.cropwindow.edge.Edge.BOTTOM.getCoordinate();

        // After this Edge is moved, our crop window is now out of proportion.
        final float targetWidth = cropper.util.AspectRatioUtil.calculateWidth(top, bottom, targetAspectRatio);
        final float currentWidth = right - left;

        // Adjust the crop window so that it maintains the given aspect ratio by
        // moving the adjacent edges symmetrically in or out.
        final float difference = targetWidth - currentWidth;
        final float halfDifference = difference / 2;
        left -= halfDifference;
        right += halfDifference;

        cropper.cropwindow.edge.Edge.LEFT.setCoordinate(left);
        cropper.cropwindow.edge.Edge.RIGHT.setCoordinate(right);

        // Check if we have gone out of bounds on the sides, and fix.
        if (cropper.cropwindow.edge.Edge.LEFT.isOutsideMargin(imageRect, snapRadius) && !mEdge.isNewRectangleOutOfBounds(cropper.cropwindow.edge.Edge.LEFT,
                                                                                                 imageRect,
                                                                                                 targetAspectRatio)) {
            final float offset = cropper.cropwindow.edge.Edge.LEFT.snapToRect(imageRect);
            cropper.cropwindow.edge.Edge.RIGHT.offset(-offset);
            mEdge.adjustCoordinate(targetAspectRatio);

        }
        if (cropper.cropwindow.edge.Edge.RIGHT.isOutsideMargin(imageRect, snapRadius) && !mEdge.isNewRectangleOutOfBounds(cropper.cropwindow.edge.Edge.RIGHT,
                                                                                                  imageRect,
                                                                                                  targetAspectRatio)) {
            final float offset = cropper.cropwindow.edge.Edge.RIGHT.snapToRect(imageRect);
            cropper.cropwindow.edge.Edge.LEFT.offset(-offset);
            mEdge.adjustCoordinate(targetAspectRatio);
        }
    }
}
