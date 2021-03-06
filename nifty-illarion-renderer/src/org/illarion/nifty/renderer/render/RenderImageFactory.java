/*
 * This file is part of the Illarion Nifty-GUI binding.
 * 
 * Copyright © 2011 - Illarion e.V.
 * 
 * The Illarion Nifty-GUI binding is free software: you can redistribute i
 * and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * 
 * The Illarion Nifty-GUI binding is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * the Illarion Nifty-GUI binding. If not, see <http://www.gnu.org/licenses/>.
 */
package org.illarion.nifty.renderer.render;

import java.util.Map;

import javolution.util.FastComparator;
import javolution.util.FastMap;

import illarion.common.util.ObjectSource;
import illarion.graphics.Sprite;
import illarion.graphics.SpriteColor;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;

/**
 * This factory takes care for the proper creation of render images for the GUI
 * of the Illarion Client.
 * 
 * @author Martin Karing
 * @since 1.22/1.3
 * @version 1.22/1.3
 */
public final class RenderImageFactory {
    /**
     * The text that is the header of a sprite image reference.
     */
    @SuppressWarnings("nls")
    private static final String HEADER_GUI_SPRITE = "guiSprite://";
    
    /**
     * The text that is the header of a dynamic image.
     */
    @SuppressWarnings("nls")
    private static final String HEADER_DYNAMIC = "dynamic://";
    
    public static interface DynamicImageSource {
        int getWidth();
        int getHeight();
        void renderImage(int x, int y, int width, int height, SpriteColor color,
            float imageScale);
        void renderImage(int x, int y, int w, int h, int srcX, int srcY,
            int srcW, int srcH, SpriteColor color, float scale, int centerX, int centerY);
    }
    
    private final Map<String, DynamicImageSource> dynamicImages;

    /**
     * This instance is a source for sprite objects that is needed to fetch
     * sprites to create render images.
     */
    private final ObjectSource<Sprite> spriteSource;

    /**
     * Constructor of the render device that takes the source of any new sprites
     * as parameter.
     * 
     * @param sprites the source of the sprites for this GUI
     */
    public RenderImageFactory(final ObjectSource<Sprite> sprites) {
        spriteSource = sprites;
        
        FastMap<String, DynamicImageSource> imageMap = new FastMap<String, DynamicImageSource>();
        imageMap.setKeyComparator(FastComparator.STRING);
        
        dynamicImages = imageMap;
    }
    
    public void addDynamicImage(final String name, final DynamicImageSource image) {
        dynamicImages.put(name, image);
    }

    /**
     * Check if the reference string is a GUI sprite string and fetch the
     * required image in case it is.
     * 
     * @param ref the reference string of the image
     * @return the image in case the string fits or <code>null</code> in case no
     *         fitting image was found or the string did not fit the syntax of a
     *         GUI sprite
     */
    private RenderImage checkAndGetSpriteImage(final String ref) {
        final Sprite sprite = getSprite(ref);
        if (sprite != null) {
            return new IllarionSpriteRenderImage(sprite);
        }
        return null;
    }
    
    private RenderImage checkAndGetDynamicImage(final String ref) {
        final DynamicImageSource image = getDynamicImage(ref);
        if (image != null) {
            return new IllarionDynamicRenderImage(image);
        }
        return null;
    }

    /**
     * Get a renderable image.
     * 
     * @param ref the string that identifies the requested image
     * @param linearFilter <code>true</code> in case simple linear filtering is
     *            fine for this image
     * @return the image or <code>null</code> in case it was not found
     */
    public RenderImage getImage(final String ref, final boolean linearFilter) {
        RenderImage ret;
        ret = checkAndGetSpriteImage(ref);
        if (ret != null) {
            return ret;
        }
        
        ret = checkAndGetDynamicImage(ref);
        if (ret != null) {
            return ret;
        }

        return null;
    }

    /**
     * Get the sprite in case the string is a proper sprite reference string.
     * 
     * @param ref the reference string
     * @return the sprite or <code>null</code>
     */
    public Sprite getSprite(final String ref) {
        if (ref.startsWith(HEADER_GUI_SPRITE)) {
            final Sprite sprite =
                spriteSource.getObject(ref.substring(HEADER_GUI_SPRITE
                    .length()));
            return sprite;
        }
        return null;
    }
    
    public DynamicImageSource getDynamicImage(final String ref) {
        if (ref.startsWith(HEADER_DYNAMIC)) {
            return dynamicImages.get(ref.substring(HEADER_DYNAMIC.length()));
        }
        return null;
    }
}
