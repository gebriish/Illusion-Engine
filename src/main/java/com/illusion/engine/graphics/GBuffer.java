package com.illusion.engine.graphics;

import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL30.*;

public class GBuffer {
    private static final int TOTAL_TEXTURES = 3;

    private int gBufferId;

    private int[] textureIds;

    private int width;

    private int height;

    public GBuffer(int w, int h) {
        // Create G-Buffer
        gBufferId = glGenFramebuffers();
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, gBufferId);

        textureIds = new int[TOTAL_TEXTURES];
        glGenTextures(textureIds);

        this.width = w;
        this.height = h;

        // Create textures for position, diffuse color, specular color, normal, shadow factor and depth
        // All coordinates are in world coordinates system
        for(int i=0; i<TOTAL_TEXTURES; i++) {
            glBindTexture(GL_TEXTURE_2D, textureIds[i]);
            int attachmentType;
            switch(i) {
                case TOTAL_TEXTURES - 1:
                    // Depth component
                    glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT32F, width, height, 0, GL_DEPTH_COMPONENT, GL_FLOAT,
                            (ByteBuffer) null);
                    attachmentType = GL_DEPTH_ATTACHMENT;
                    break;
                default:
                    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB32F, width, height, 0, GL_RGBA, GL_FLOAT, (ByteBuffer) null);
                    attachmentType = GL_COLOR_ATTACHMENT0 + i;
                    break;
            }
            // For sampling
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);

            // Attach the texture to the G-Buffer
            glFramebufferTexture2D(GL_FRAMEBUFFER, attachmentType, GL_TEXTURE_2D, textureIds[i], 0);
        }

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer intBuff = stack.mallocInt(TOTAL_TEXTURES);
            for(int i = 0; i < textureIds.length; i++) {
                intBuff.put(GL_COLOR_ATTACHMENT0 + i);
            }
            intBuff.flip();
            glDrawBuffers(intBuff);
        }

        // Unbind
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getGBufferId() {
        return gBufferId;
    }

    public int[] getTextureIds() {
        return textureIds;
    }

    public int getDepthTexture() {
        return textureIds[TOTAL_TEXTURES-1];
    }

    public void cleanUp() {
        glDeleteFramebuffers(gBufferId);

        if (textureIds != null) {
            for (int i=0; i<TOTAL_TEXTURES; i++) {
                glDeleteTextures(textureIds[i]);
            }
        }
    }

    public void bind_framebuffer() {
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, getGBufferId());
        glViewport(0, 0, width, height);
    }

    public void unbind_framebuffer() {
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
    }

    public void activateTextures(int index) {
        for(int i=0; i<textureIds.length-1; i++)
        {
            glActiveTexture(GL_TEXTURE0 + i + index);
            glBindTexture(GL_TEXTURE_2D, textureIds[i]);
        }
    }

    public void resize(int width, int height) {
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, gBufferId);

        for(int i=0; i<TOTAL_TEXTURES; i++) {
            glBindTexture(GL_TEXTURE_2D, textureIds[i]);
            int attachmentType;
            switch(i) {
                case TOTAL_TEXTURES - 1:
                    // Depth component
                    glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT32F, width, height, 0, GL_DEPTH_COMPONENT, GL_FLOAT,
                            (ByteBuffer) null);
                    attachmentType = GL_DEPTH_ATTACHMENT;
                    break;
                default:
                    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB32F, width, height, 0, GL_RGBA, GL_FLOAT, (ByteBuffer) null);
                    attachmentType = GL_COLOR_ATTACHMENT0 + i;
                    break;
            }
            // For sampling
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

            // Attach the texture to the G-Buffer
            glFramebufferTexture2D(GL_FRAMEBUFFER, attachmentType, GL_TEXTURE_2D, textureIds[i], 0);
        }



        // Unbind
        glBindFramebuffer(GL_FRAMEBUFFER, 0);

        this.width = width;
        this.height = height;
    }
}

