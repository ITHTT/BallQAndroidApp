package com.tysci.ballq.modles;

import android.os.Parcel;
import android.os.Parcelable;

import com.tysci.ballq.utils.KLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2016/5/1.
 */
public class BallQMatchLeagueEntity implements Parcelable {
    private int id;
    private String name;
    private boolean isChecked;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeByte(isChecked ? (byte) 1 : (byte) 0);
    }

    public BallQMatchLeagueEntity() {
    }

    private BallQMatchLeagueEntity(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.isChecked = in.readByte() != 0;
    }

    public static final Creator<BallQMatchLeagueEntity> CREATOR = new Creator<BallQMatchLeagueEntity>() {
        public BallQMatchLeagueEntity createFromParcel(Parcel source) {
            return new BallQMatchLeagueEntity(source);
        }

        public BallQMatchLeagueEntity[] newArray(int size) {
            return new BallQMatchLeagueEntity[size];
        }
    };


    public static List<BallQMatchLeagueEntity> getBallQMatchLeagueInfos(String response){
        try {
            List<BallQMatchLeagueEntity> leagueEntities=null;
            JSONObject object=new JSONObject(response);
            if(object!=null){
                JSONObject dataObj=object.getJSONObject("data");
                if(dataObj!=null){
                    JSONArray leagues=dataObj.getJSONArray("tournaments");
                    if(leagues!=null){
                        int size=leagues.length();
                        if(size>0) {
                            leagueEntities=new ArrayList<>(size);
                            BallQMatchLeagueEntity info=null;
                            for (int i = 0; i < size; i++) {
                                Iterator<String> keys = leagues.getJSONObject(i).keys();
                                String key=keys.next();
                                String value=leagues.getJSONObject(i).getString(key);
                                KLog.e("key:" + key + ",value:" + value);
                                info=new BallQMatchLeagueEntity();
                                info.setId(Integer.parseInt(key));
                                info.setName(value);
                                leagueEntities.add(info);
                            }
                            return leagueEntities;
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
