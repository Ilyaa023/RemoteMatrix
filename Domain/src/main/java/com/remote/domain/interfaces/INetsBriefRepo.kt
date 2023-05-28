package com.remote.domain.interfaces

import com.remote.domain.models.NetBrief

interface INetsBriefRepo {
    fun getNets(callback: (ArrayList<NetBrief>) -> Unit)
    fun setBusy(net: NetBrief, deviceName: String)
    fun getNet(net: NetBrief, callback: (NetBrief) -> Unit)
}