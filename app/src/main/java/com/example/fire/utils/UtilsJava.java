package com.example.fire.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.Editable;
import android.util.Log;
import android.util.TypedValue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TruyenDev on 30/10/2022.
 */
public class UtilsJava {
    public static boolean isNullOrEmpty(Object model) {
        if (model == null) {
            return true;
        } else if (model instanceof Editable) {
            Editable editable = (Editable) model;
            return isNullOrEmpty(editable.toString());
        } else if (model instanceof String) {
            String string = (String) model;
            return string.trim().length() == 0 || "null".equalsIgnoreCase(string.trim());
        } else if (model instanceof CharSequence) {
            CharSequence charSequence = (CharSequence) model;
            return isNullOrEmpty(charSequence.toString());
        } else if (model instanceof ArrayList) {
            ArrayList arrayList = (ArrayList) model;
            return arrayList.isEmpty();
        } else if (model instanceof List) {
            List list = (List) model;
            return list.isEmpty();
        } else if (model instanceof HashMap) {
            HashMap hashMap = (HashMap) model;
            return hashMap.isEmpty();
        } else {
            return false;
        }
    }

    /**
     *
     */
    public static File bitmapToFile(Activity activity, Bitmap bitmap, int typePhoto, int maxSizeKb, String nameImage, boolean isSaved) {
        if (bitmap == null) {
            return null;
        }
        //create a file to write bitmap data
        String fileNameToSave = "BVDR_" + System.currentTimeMillis() + "_";


//      Save a file: path for use with ACTION_VIEW intents
        File file;
        if (isSaved) {
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + fileNameToSave + ".jpg");
        } else {
            file = new File(activity.getCacheDir().getAbsolutePath() + "/" + fileNameToSave + ".jpg");
        }
        if (file == null) return null;
        file.getParentFile().mkdirs();
        int MAX_IMAGE_SIZE = maxSizeKb * 1024;
        int compressQuality = 110;
        int streamLength;
        ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
        do {
            try {
                bmpStream.flush();
                bmpStream.reset();
            } catch (Exception e) {
                Log.e("TAG", "compressFile: ", e);
            }
            compressQuality -= 10;
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream);
            byte[] bmpPicByteArray = bmpStream.toByteArray();
            streamLength = bmpPicByteArray.length;
        } while (maxSizeKb > 0 && streamLength >= MAX_IMAGE_SIZE && compressQuality > 10);

        try {
            FileOutputStream fo = new FileOutputStream(file);
            fo.write(bmpStream.toByteArray());
            fo.flush();
            fo.close();
        } catch (Exception e) {
            file = null;
        }
        return file;
    }
    /**
     *
     */
//    public static String convertStringVNtoENLowerCase(String valueVN) {
//        if (valueVN == null) return "";
//        String temp = Normalizer.normalize(valueVN, Normalizer.Form.NFKD);
//        Pattern pattern = Pattern.compile("\\p{InCOMBINING_DIACRITICAL_MARKS}+");
//        String valueEN = pattern.matcher(temp).replaceAll("")
//                .replaceAll("đ", "d")
//                .replaceAll("Đ", "D");
//        return removeSpacesAndLowerCase(valueEN);
//    }

//    public static String removeSpacesAndLowerCase(String valueVN) {
//        if (valueVN == null) return "";
//        return removeSpaces(valueVN).toLowerCase();
//    }
//    public void adjustFontScale(Configuration configuration ){
//        configuration.fontScale = (float) 1.0;
//        DisplayMetrics metrics = getResources().getDisplayMetrics();
//        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
//        wm.getDefaultDisplay().getMetrics(metrics);
//        metrics.scaledDensity = configuration.fontScale * metrics.density;
//        configuration.densityDpi = (int) getResources().getDisplayMetrics().xdpi;
//        getBaseContext().getResources().updateConfiguration(configuration, metrics);
//    }
//    public  void preventChangeFontSize(){
//        Configuration configuration =    getResources().getConfiguration();
//        onConfigurationChanged(configuration);
//    }

//TODO
    //upload photo
//    List<MultipartBody.Part> listBody = new ArrayList<>();
//    MediaType MEDIA_TYPE = MediaType.parse("image/*");
//        listBody.add(MultipartBody.Part.createFormData("file",
//                Helper.convertStringVNtoENLowerCase(file.getName()),
//                RequestBody.create(MEDIA_TYPE, file)));
//
//    @Multipart
//    @POST("api/upload/image")
//    Call<UploadImageResponse> uploadImage(@Part List<MultipartBody.Part> photo);

//    public static File compressFile(Context context, String path, int maxSizeKb, String name) {
//        String nameFile = getRandomName();
//        if (name != null && !"null".equals(name)) {
//            nameFile = name;
//        }
//        File file = new File(context.getCacheDir().getAbsolutePath() + "/" + nameFile + ".jpg");
//
//        Glide.with(context.getApplicationContext())
//                .asBitmap()
//                .load(path)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
//                .apply(new RequestOptions().override(1080, 1920))
//                .listener(new RequestListener<Bitmap>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
//                        // resource is your loaded Bitmap
//                        Bitmap original = resource;
//                        if (original != null) {
//                            original = scaleImage(original, 1080, 1920);
//                            int MAX_IMAGE_SIZE = maxSizeKb * 1024;
//                            int streamLength = MAX_IMAGE_SIZE;
//                            int compressQuality = 105;
//                            ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
//                            while (streamLength >= MAX_IMAGE_SIZE && compressQuality > 10) {
//                                try {
//                                    bmpStream.flush();//to avoid out of memory error
//                                    bmpStream.reset();
//                                } catch (Exception e) {
//                                    recordException(e);
//                                    Log.e(TAG, "compressFile: ", e);
//                                }
//                                compressQuality -= 10;
//                                original.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream);
//                                byte[] bmpPicByteArray = bmpStream.toByteArray();
//                                streamLength = bmpPicByteArray.length;
//                            }
//
//                            FileOutputStream fo;
//
//                            try {
//                                fo = new FileOutputStream(file);
//                                fo.write(bmpStream.toByteArray());
//                                fo.flush();
//                                fo.close();
//                            } catch (Exception e) {
//                                recordException(e);
//                                Log.e(TAG, "compressFile: ", e);
//                            }
//                        } else {
//                            recordException(new Exception("Helper.compressFile() null bitmap"));
//                        }
//                        return true;
//                    }
//                }).submit();
//
//        return file;
//    }


    public static String removeAllSpaces(String string, boolean isLowerCase) {
        if (isNullOrEmpty(string)) {
            return "";
        } else if (isLowerCase) {
            return string.trim().replaceAll(" ", "").toLowerCase();
        } else {
            return string.trim().replaceAll(" ", "").toUpperCase();
        }
    }

    private static final String[] SOURCE_CHARACTERS = {"A", "Á", "À", "Ả", "Ã", "Ạ", "Ă", "Ắ", "Ằ", "Ẳ", "Ẵ", "Ặ", "Â", "Ấ", "Ầ", "Ẩ", "Ẫ", "Ậ", "Đ", "E", "É", "È", "Ẻ", "Ẽ", "Ẹ", "Ê", "Ế", "Ề", "Ể", "Ễ", "Ệ", "I", "Í", "Ì", "Ỉ", "Ĩ", "Ị", "O", "Ó", "Ò", "Ỏ", "Õ", "Ọ", "Ô", "Ố", "Ồ", "Ổ", "Ỗ", "Ộ", "Ơ", "Ớ", "Ờ", "Ở", "Ỡ", "Ợ", "U", "Ú", "Ù", "Ủ", "Ũ", "Ụ", "Ư", "Ứ", "Ừ", "Ử", "Ữ", "Ự", "a", "á", "à", "ả", "ã", "ạ", "ă", "ắ", "ằ", "ẳ", "ẵ", "ặ", "â", "ấ", "ầ", "ẩ", "ẫ", "ậ", "đ", "e", "é", "è", "ẻ", "ẽ", "ẹ", "ê", "ế", "ề", "ể", "ễ", "ệ", "i", "í", "ì", "ỉ", "ĩ", "ị", "o", "ó", "ò", "ỏ", "õ", "ọ", "ô", "ố", "ồ", "ổ", "ỗ", "ộ", "ơ", "ớ", "ờ", "ở", "ỡ", "ợ", "u", "ú", "ù", "ủ", "ũ", "ụ", "ư", "ứ", "ừ", "ử", "ữ", "ự"};
    private static final String[] DESTINATION_CHARACTERS = {"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "D", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "I", "I", "I", "I", "I", "I", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "U", "U", "U", "U", "U", "U", "U", "U", "U", "U", "U", "U", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "d", "e", "e", "e", "e", "e", "e", "e", "e", "e", "e", "e", "e", "i", "i", "i", "i", "i", "i", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "u", "u", "u", "u", "u", "u", "u", "u", "u", "u", "u", "u",};
    private static <K, V> Map<K, V> mapFromArrays(K[] keys, V[] values) {
        HashMap<K, V> result = new HashMap<>();
        for (int i = 0; i < keys.length; i++) {
            result.put(keys[i], values[i]);
        }
        return result;

    }

    private static String removeAccent(String str) {
        if (isNullOrEmpty(str)) {
            return "";
        } else {
            for (int i = 0; i < str.length(); i++) {
                Map<String, String> map = mapFromArrays(SOURCE_CHARACTERS, DESTINATION_CHARACTERS);
                if (map.containsKey(String.valueOf(str.charAt(i)))) {
                    str = str.replaceAll(String.valueOf(str.charAt(i)), map.get(String.valueOf(str.charAt(i))));
                }
            }
            return str;
        }
    }
    public static int dpToSp(float dp, Context context) {
        return (int) (dpToPx(dp, context) / context.getResources().getDisplayMetrics().scaledDensity);
    }
    public static int dpToPx(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}


