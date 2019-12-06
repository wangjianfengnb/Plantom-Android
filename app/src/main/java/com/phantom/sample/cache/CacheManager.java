package com.phantom.sample.cache;

import android.content.Context;

import com.phantom.sample.App;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class CacheManager {

    /**
     * 保存对象
     *
     * @param ser  object
     * @param file file key
     */
    public static boolean saveObject(Serializable ser, String file) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = App.getContext().openFileOutput(file, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(ser);
            oos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
            } catch (Exception ignored) {
            }
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * 读取对象，默认不判断过期时间
     *
     * @param file file key
     */
    public static Serializable readObject(String file) {
        return readObject(file, 0);
    }

    /**
     * 读取对象
     *
     * @param file       file key
     * @param expireTime 0表示不判断过期时间
     * @return 对象
     */
    public static Serializable readObject(String file, final long expireTime) {
        if (!isExistDataCache(App.getContext(), file)) {
            return null;
        }
        if (isDataTimeOut(App.getContext(), file, expireTime) && expireTime != 0) {
            return null;
        }
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = App.getContext().openFileInput(file);
            ois = new ObjectInputStream(fis);
            return (Serializable) ois.readObject();
        } catch (FileNotFoundException ignored) {
        } catch (Exception e) {
            e.printStackTrace();
            // 反序列化失败 - 删除缓存文件
            if (e instanceof InvalidClassException) {
                File data = App.getContext().getFileStreamPath(file);
                if (data.delete()) {
                    return null;
                }
            }
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (Exception ignored) {
            }
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    /**
     * 判断缓存是否过期
     *
     * @param context context
     * @param file    file name
     * @return true is timeout otherwise is valid
     */
    private static boolean isDataTimeOut(Context context, String file, long expireTime) {
        File data = context.getFileStreamPath(file);
        long time = data.lastModified();
        return System.currentTimeMillis() - time > expireTime;
    }

    /**
     * 判断缓存是否存在
     *
     * @param cacheFile cache key
     * @return cache is exists
     */
    private static boolean isExistDataCache(Context context, String cacheFile) {
        if (context == null) {
            return false;
        }
        boolean exist = false;
        File data = context.getFileStreamPath(cacheFile);
        if (data.exists()) {
            exist = true;
        }
        return exist;
    }

}
