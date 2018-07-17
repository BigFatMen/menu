/*
 * Copyright (C) Matthew Steglinski (SainttX) <matt@ipvp.org>
 * Copyright (C) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package me.missionary.menu.mask;


import me.missionary.menu.Menu;
import me.missionary.menu.button.Button;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Missionary (missionarymc@gmail.com)
 * @since 5/17/2018
 */
public class Mask2D implements Mask {

    private final Map<Character, Button> maskButtonKey;
    private String maskPattern;

    public Mask2D() {
        this.maskButtonKey = new HashMap<>();
    }

    @Override
    public Mask setButton(char key, Button button) {
        maskButtonKey.put(key, button);
        return this;
    }

    @Override
    public Mask setMaskPattern(String... maskPattern) {
        String concatPattern = Arrays.stream(maskPattern).collect(Collectors.joining());
        for (char c : concatPattern.toCharArray()) {
            if (Character.isWhitespace(c)) {
                continue;
            }
            if (!maskButtonKey.containsKey(c)) {
                throw new IllegalArgumentException(String.format("%s is a unrecognized mapping for the Mask.", c));
            }
        }
        this.maskPattern = concatPattern.replace("\n", "");
        return this;
    }

    @Override
    public void applyTo(Menu menu) {
        if (maskButtonKey.isEmpty()) {
            throw new IllegalArgumentException("The maskButtonKey map is empty!");
        }
        if (maskPattern == null) {
            throw new IllegalArgumentException("The maskPattern is null!");
        }
        if (maskPattern.length() > menu.getMenuDimension().getSize()) {
            throw new IllegalArgumentException(String.format("The maskPattern length: %d is longer than the menu dimension: %d", maskPattern.length(), menu.getMenuDimension().getSize()));
        }

        IntStream.range(0, maskPattern.length()).forEach(i -> {
            char ch = maskPattern.charAt(i);
            if (ch == ' ' || ch == '_') {
                menu.setItem(i, null);
            } else {
                menu.setItem(i, maskButtonKey.get(ch));
            }
        });
    }
}