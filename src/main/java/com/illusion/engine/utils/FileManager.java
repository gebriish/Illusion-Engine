package com.illusion.engine.utils;


import com.illusion.engine.client.Debug;
import com.illusion.engine.client.LogLevel;
import com.illusion.engine.graphics.Shader;
import com.illusion.engine.graphics.Texture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static org.lwjgl.BufferUtils.createByteBuffer;

public class FileManager {

    private static Map<String, Texture> m_TextureList;
    private static Map<String, Shader> m_ShaderList;

    static
    {
        m_TextureList = new HashMap<>();
        m_ShaderList = new HashMap<>();
    }

    public static Shader loadShader(String locFileName) {
        String location = new File(locFileName).getAbsolutePath();

        if(m_ShaderList.containsKey(location))
        {
            return m_ShaderList.get(location);
        }else
        {
            Shader s = new Shader(FileManager.loadFileToString(locFileName + ".vert"), FileManager.loadFileToString(locFileName + ".frag"));
            m_ShaderList.put(location, s);
            return s;
        }
    }

    public static Texture loadTexture(String loc, int filtering, int mipmap) {
        if(m_TextureList.containsKey(loc))
        {
            return m_TextureList.get(loc);
        }else
        {
            Texture t = Texture.loadTexture(loc, filtering);
            t.name = new File(loc).getName();
            switch(mipmap)
            {
                case 1 :
                    t.LNMipmap();
                    break;
                case 2:
                    t.LLMipmap();
                    break;
                case 3:
                    t.NLMipmap();
                    break;
                case 4:
                    t.NNMipmap();
                    break;
            }
            m_TextureList.put(new File(loc).getAbsolutePath(), t);
            return t;
        }
    }

    public static ByteBuffer ExtractByteBufferFromImagePath(String s) {
        try {
            BufferedImage bi = ImageIO.read(new java.io.File(s));
            byte[] iconData = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
            ByteBuffer ib = createByteBuffer(iconData.length);
            ib.order(ByteOrder.nativeOrder());
            ib.put(iconData, 0, iconData.length);
            ib.flip();
            return ib;
        }
        catch (Exception e){
            System.out.println("Couldn't open icon image..." + e.toString());
            return null;
        }
    }

    public static FileType getType(String fileName) {
        String extension;
        int index = fileName.lastIndexOf('.');
        if(index > 0){
            extension = fileName.substring(index + 1);
        } else {
            extension = "\0";
        }

        switch (extension)
        {
            // images
            case "png" -> {
                return FileType.PNG;
            }
            case "jpeg" -> {
                return FileType.JPEG;
            }
            case "jpg" -> {
                return FileType.JPG;
            }

            //shaders
            case "frag" -> {
                return FileType.FRAG;
            }
            case "vert" -> {
                return FileType.VERT;
            }
            case "glsl" -> {
                return FileType.GLSL;
            }

            //serialization
            case "yaml" ->{
                return FileType.YAML;
            }
            case "json" ->{
                return FileType.JSON;
            }
            case "ini" ->{
                return FileType.INI;
            }

            // non supported file format
            default -> {
                return FileType.NONE;
            }
        }
    }

    public static boolean isImage(FileType type) {
        if(
                   type == FileType.PNG
                || type == FileType.JPEG
                || type == FileType.JPG
        )
            return true;
        else
            return false;
    }

    public static String loadFileToString(String fileLoc) {
        Path filePath = Path.of(fileLoc);
        String content = "\0";
        try {
            content = Files.readString(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return content;
    }

    public static void DeleteTexture(File f) {
        String loc = f.getAbsolutePath();
        if(m_TextureList.containsKey(loc))
        {
            m_TextureList.get(loc).delete();
            m_TextureList.remove(loc);
        }else {
            Debug.Log(LogLevel.WARNING, "Texture delete request failure: File Manager");
        }
    }

    public static void Flush() {
        m_TextureList.clear();
    }
}
