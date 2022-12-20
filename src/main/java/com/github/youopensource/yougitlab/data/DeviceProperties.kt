package com.github.youopensource.yougitlab.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

@Generated("jsonschema2pojo")
class DeviceProperties(
    @SerializedName("screen_width")
    @Expose var screenWidth: Int?, @SerializedName("screen_height")
    @Expose var screenHeight: Int?, @SerializedName("viewport_width")
    @Expose var viewportWidth: Int?, @SerializedName("viewport_height")
    @Expose var viewportHeight: Int?, @SerializedName("beacon_active")
    @Expose var beaconActive: Boolean?
)
