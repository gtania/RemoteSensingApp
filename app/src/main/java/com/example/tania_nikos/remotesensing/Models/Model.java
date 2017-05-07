package com.example.tania_nikos.remotesensing.Models;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Tania-Nikos on 4/5/2017.
 */

public class Model {

    /**
     * Get date now
     *
     * @return
     */
    protected String getDateNow()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
}
