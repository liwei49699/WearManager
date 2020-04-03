package com.chengzhen.wearmanager.manager;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.module.AppGlideModule;

import java.io.File;

@GlideModule
public class BaseGlideModule extends AppGlideModule {

    private final static int cacheSize100MegaBytes = 100 * 1024 * 1024;
    public static final String IMG_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "pic";

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDiskCache(new ExternalPreferredCacheDiskCacheFactory(context, "pic", cacheSize100MegaBytes));
        boolean permission = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permission = context.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !permission) {
            builder.setDiskCache(new InternalCacheDiskCacheFactory(context, cacheSize100MegaBytes));
        } else {
            File cacheLocation = new File(IMG_PATH);
            if (!cacheLocation.exists()) {
                cacheLocation.mkdirs();
            }
            boolean read = cacheLocation.canRead();
            if (read) {
                builder.setDiskCache(new DiskLruCacheFactory(cacheLocation.getPath(), cacheSize100MegaBytes));
            } else {
                builder.setDiskCache(new InternalCacheDiskCacheFactory(context, cacheSize100MegaBytes));
            }

        }


    }
}
