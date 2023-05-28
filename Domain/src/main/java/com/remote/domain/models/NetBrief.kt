package com.remote.domain.models

data class NetBrief(
    val id: String,
    val countOfBMNs: Int,
    val connectedDevices: ArrayList<String>
) {
    companion object{
        val NETS = "netsBrief"
    }
}