/*
 * Copyright (c) 2018 Nuvolect LLC.
 * This software is offered for free under conditions of the GPLv3 open source software license.
 * Contact Nuvolect LLC for a less restrictive commercial license if you would like to use the software
 * without the GPLv3 restrictions.
 */

package com.nuvolect.deepdive.show;//

import com.nuvolect.deepdive.util.OmniFile;

import java.util.ArrayList;

/**
 * Shared data across gallery, image slider and commands
 */
public class Data {

    public static final int SHARE_RESULT_ID = 31415;
    /**
     * Set of files in the current directory.
     * If the directory is not root and element 0 is the parent directory
     */
    public static ArrayList<OmniFile> m_files;
    /**
     * Set of names for files in the current directory.
     * If the directory is not root, element 0 name is the parent, name "..".
     */
    public static ArrayList<String> m_names;
    /**
     * Volume ID of the current volume.
     */
    public static String m_volumeId;


    /**
     * Set index or positions that the user has selected.
     * The screen slide activity will only select one file at a time.
     */
    public static int m_selectedPosition;
    public static String m_selectedName;

    /**
     * File in app private data used when sharing.
     * It is deleted in the onActivityResult
     */
    public static OmniFile m_privateFile = null;


    /**
     * Configure gallery data structures.  Data is shared with ScreenSlide.
     * Two array lists are kept in sync.
     * Files has the list of files.
     * Names has the list of names.
     * @param dirFile
     */
    public static void setFiles(OmniFile dirFile) {

        OmniFile[] dirFiles = dirFile.listFiles();
        int length = dirFiles.length;
        OmniFile parent = null;

        if (! dirFile.isRoot()) {
            ++length;
            parent = dirFile.getParentFile();
        }

        Data.m_files = new ArrayList<>( length );
        Data.m_names = new ArrayList<>( length );

        if( parent != null){

            Data.m_files.add( parent );
            Data.m_names.add( "..");
        }

        for ( OmniFile file : dirFiles){

            Data.m_files.add( file );

            if( file.isDirectory())
                Data.m_names.add( "["+file.getName()+"]");
            else
                Data.m_names.add( file.getName());
        }
    }
}
