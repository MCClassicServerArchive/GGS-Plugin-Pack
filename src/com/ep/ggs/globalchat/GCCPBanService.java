/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.globalchat;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import com.ep.ggs.iomodel.Player;
import com.ep.ggs.system.Serializer;
import com.ep.ggs.system.Serializer.SaveType;


public class GCCPBanService {
    private static GCCPBan[] ban_list;
    private static final Serializer<GCCPBan[]> LOADER = new Serializer<GCCPBan[]>(SaveType.JSON, 0L);
    private static final String BAN_LIST_URL = "http://server.mcforge.net/gcbanned.txt";

    public static void updateBanList() {
        try {
            InputStream input = new URL(BAN_LIST_URL).openStream();
            ban_list = LOADER.getObject(input, GCCPBan[].class);
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public static GCCPBan getPlayerBan(Player player) {
        for (GCCPBan ban : ban_list) {
            if (ban.isBanIP()) {
                if (player.getIP().equals(ban.getBanName()))
                    return ban;
            }
            else {
                if (player.getName().equals(ban.getBanName()))
                    return ban;
            }
        }
        return null;
    }
    
    public static boolean isPlayerBanned(Player player) {
        return getPlayerBan(player) != null;
    }



    public class GCCPBan {
        private int banned_id;
        private String banned_name;
        private String banned_by;
        private String banned_reason;
        private long banned_time;
        private int banned_isIp;

        public GCCPBan() { }

        public int getBanID() {
            return banned_id;
        }

        public String getBanName() {
            return banned_name;
        }

        public String getBannedBy() {
            return banned_by;
        }

        public String getBanReason() {
            return banned_reason;
        }

        public Date getBanDate() {
            return new Date(banned_time);
        }

        public boolean isBanIP() {
            return banned_isIp == 1;
        }
    }

}
