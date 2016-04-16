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
 * HandleHelper class to handle the center handle.
 */
class CenterHandleHelper extends cropper.cropwindow.handle.HandleHelper {

    // Constructor /////////////////////////////////////////////////////////////

    CenterHandleHelper() {
        super(null, null);
    }

    // HandleHelper Methods ////////////////////////////////////////////////////

    @Override
    void updateCropWindow(float x,
                          float y,
                          Rect imageRect,
                          float snapRadius) {

        float left = cropper.cropwindow.edge.Edge.LEFT.getCoordinate();
        float top = cropper.cropwindow.edge.Edge.TOP.getCoordinate();
        float right = cropper.cropwindow.edge.Edge.RIGHT.getCoordinate();
        float bottom = cropper.cropwindow.edge.Edge.BOTTOM.getCoordinate();

        final float currentCenterX = (left + right) / 2;
        final float currentCenterY = (top + bottom) / 2;

        final float offsetX = x - currentCenterX;
        final float offsetY = y - currentCenterY;

        // Adjust the crop window.
        cropper.cropwindow.edge.Edge.LEFT.offset(offsetX);
        cropper.cropwindow.edge.Edge.TOP.offset(offsetY);
        cropper.cropwindow.edge.Edge.RIGHT.offset(offsetX);
        cropper.cropwindow.edge.Edge.BOTTOM.offset(offsetY);

        // Check if we have gone out of bounds on the sides, and fix.
        if (cropper.cropwindow.edge.Edge.LEFT.isOutsideMargin(imageRect, snapRadius)) {
            final float offset = cropper.cropwindow.edge.Edge.LEFT.snapToRect(imageRect);
            cropper.cropwindow.edge.Edge.RIGHT.offset(offset);
        } else if (cropper.cropwindow.edge.Edge.RIGHT.isOutsideMargin(imageRect, snapRadius)) {
            final float offset = cropper.cropwindow.edge.Edge.RIGHT.snapToRect(imageRect);
            cropper.cropwindow.edge.Edge.LEFT.offset(offset);
        }

        // Check if we have gone out of bounds on the top or bottom, and fix.
        if (cropper.cropwindow.edge.Edge.TOP.isOutsideMargin(imageRect, snapRadius)) {
            final float offset = cropper.cropwindow.edge.Edge.TOP.snapToRect(imageRect);
            cropper.cropwindow.edge.Edge.BOTTOM.offset(offset);
        } else if (cropper.cropwindow.edge.Edge.BOTTOM.isOutsideMargin(imageRect, snapRadius)) {
            final float offset = cropper.cropwindow.edge.Edge.BOTTOM.snapToRect(imageRect);
            cropper.cropwindow.edge.Edge.TOP.offset(offset);
        }
    }

    @Override
    void updateCropWindow(float x,
                          float y,
                          float targetAspectRatio,
                          Rect imageRect,
                          float snapRadius) {

        updateCropWindow(x, y, imageRect, snapRadius);
    }
}
