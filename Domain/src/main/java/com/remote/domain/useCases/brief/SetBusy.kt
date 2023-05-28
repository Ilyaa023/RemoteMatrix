package com.remote.domain.useCases.brief

import android.os.Build
import com.remote.domain.interfaces.INetsBriefRepo
import com.remote.domain.models.NetBrief

class SetBusy (private val repo: INetsBriefRepo) {
    fun execute (net: NetBrief, callback: (success: Boolean) -> Boolean){
        repo.getNet(net){
            if (it.connectedDevices.size == 0) {
                repo.setBusy(net, Build.MANUFACTURER + Build.MODEL)
                callback(true)
            }
            else
                if (callback(false))
                    repo.setBusy(net, Build.MANUFACTURER + Build.MODEL)
        }
    }
}