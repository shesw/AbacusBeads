package com.sheswland.abacusbeads.utils;

import com.sheswland.abacusbeads.utils.work.WorkManager;
import com.sheswland.abacusbeads.utils.work.utils.OneTimeWorkRequest;
import com.sina.cloudstorage.auth.AWSCredentials;
import com.sina.cloudstorage.auth.BasicAWSCredentials;
import com.sina.cloudstorage.services.scs.SCS;
import com.sina.cloudstorage.services.scs.SCSClient;
import com.sina.cloudstorage.services.scs.model.ObjectListing;

import java.net.URL;
import java.util.Date;

public class SinaUtils {

    private final String TAG = "SinaUtils";
    private static SinaUtils _holder;
    public synchronized static SinaUtils getInstance() {
        if (_holder == null) {
            _holder = new SinaUtils();
        }
        return _holder;
    }

    private AWSCredentials credentials;
    private SCS conn;
    private volatile boolean isReady;

    public synchronized void init() {
        WorkManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                credentials = new BasicAWSCredentials(SinaConfig.accessKey, SinaConfig.secretKey);
                conn = new SCSClient(credentials);
                isReady = true;
            }
        });
    }

    public boolean isReady() {return isReady;}

    public String generateUrlByDefaultWithoutParams(String path){
        String totalPath = generateUrlByDefault(path);
        return totalPath.substring(0, totalPath.indexOf("?"));
    }

    //need run on sub thread
    public String generateUrlByDefault(String path){
        return generateUrl(SinaConfig.bucketName, path, SinaConfig.expiration_min);
    }

    //need run on sub thread
    public String generateUrl(String bucketName, String path, int minutes){
        if (!isReady) return "";
        Date expiration = new Date();
        long epochMillis = expiration.getTime();
        epochMillis += 1000*60*minutes;
        expiration = new Date(epochMillis);
        URL presignedUrl = conn.generatePresignedUrl(bucketName, path, expiration, false);
        return presignedUrl.toString();
    }
    //need run on sub thread
    public ObjectListing listObjects(String bucketName) {
        return listObjects(bucketName, "");
    }

    //need run on sub thread
    public ObjectListing listObjects(String bucketName, String nextMark){
        if (!isReady) return null;
        ObjectListing objectListing = TextUtil.isEmpty(nextMark) ? conn.listObjects(bucketName) : conn.listObjects(bucketName, nextMark);
        System.out.println(objectListing);
        return objectListing;
    }


}
