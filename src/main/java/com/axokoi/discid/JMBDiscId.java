/*
 * Copyright (C) 2012 Dietmar Steiner <jmusicbrainz [at] d-steiner.com>.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
/**
 * @author Dietmar Steiner
 */
package com.axokoi.discid;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;

/**
 * @author Dietmar Steiner
 * maintainer: Check at github/axokoi
 */
public class JMBDiscId {

    private static Log log = LogFactory.getLog(JMBDiscId.class);
    private static LibDiscId libDiscId = null;
    private Pointer disc = null;

    /**
     * Loads the library only when it is not already loaded and allocates a
     * local disc object;
     *
     * @param path path to libdiscid library
     * @return <code>true</code> if successful else <code>false</code>
     */
    public synchronized boolean init(String path) {
        if (!new File(path).exists()) {
            log.fatal("File: " + path + " does not exist");
        } else {
            if (libDiscId == null) {
                try {
                    libDiscId = (LibDiscId) Native.loadLibrary(path, LibDiscId.class);
                } catch (Throwable ex) {
                    log.fatal("Check you running are the correct library for your architecture: 64 or 32 bit", ex);
                }
            }
            if (libDiscId != null && disc == null) {
                disc = disc = libDiscId.discid_new();
            }
        }
        return libDiscId != null;
    }

    /**
     * Reads the disc and generates a MusicBrainz DiscId
     *
     * @param drive path to the drive with the audio CD
     * @return the MusicBrainz DiscId or <code>null</code> if unsuccessful
     */
    public String getDiscId(String drive) {
        String ret = null;
        boolean success = libDiscId.discid_read(disc, drive);
        if (!success) {
            log.fatal(libDiscId.discid_get_error_msg(disc).getString(0));
        } else {
            ret = libDiscId.discid_get_id(disc).getString(0);
        }
        return ret;
    }

    /**
     * Reads the disc and generates a FreeDB DiscId
     *
     * @param drive path to the drive with the audio CD
     * @return the FreeDB DiscId or <code>null</code> if unsuccessful
     */
    public String getFreeDBId(String drive) {
        String ret = null;
        boolean success = libDiscId.discid_read(disc, drive);
        if (!success) {
            log.fatal(libDiscId.discid_get_error_msg(disc).getString(0));
        } else {
            ret = libDiscId.discid_get_freedb_id(disc).getString(0);
        }
        return ret;
    }

    /**
     * Reads the drive and generates a MusicBrainz submittion url
     *
     * @param drive path to the drive with the audio CD
     * @return the MusicBrainz submition url or <code>null</code> if
     * unsuccessful
     */
    public String getSubmissionUrl(String drive) {
        String ret = null;
        boolean success = libDiscId.discid_read(disc, drive);
        if (!success) {
            log.fatal(libDiscId.discid_get_error_msg(disc).getString(0));
        } else {
            ret = libDiscId.discid_get_submission_url(disc).getString(0);
        }
        return ret;

    }

    /**
     * Reads the drive and generates a MusicBrainz DiscId lookup url.
     *
     * @param drive path to the drive with the audio CD
     * @return the MusicBrainz discId lookup url or <code>null</code> if
     * unsuccessful
     */
    public String getDiscIdLookupUrl(String drive) {
        String ret = null;
        String discId = getDiscId(drive);
        String toc = getSubmissionUrl(drive);
        if (discId != null && toc != null) {
            toc = toc.substring(toc.indexOf('&'));
            ret = "http://www.musicbrainz.org/ws/2/discid/" + discId + "?" + toc;
        }
        return ret;
    }

    /**
     *
     * @param drive path to the drive with the audio CD
     * @return The Table of Content as a String or an empty String if an error happened.
     * As from Libdiscid doc:
     * The string has following values separated by space:
     * first track number last - track number - total length in sectors - offset of 1st track offset of 2nd track ...
     *
     * Example: 1 7 164900 150 22460 50197 80614 100828 133318 144712
     */
    public String getToC(String drive) {
        boolean success = libDiscId.discid_read(disc, drive);
        if (!success) {
            log.fatal(libDiscId.discid_get_error_msg(disc).getString(0));
            return "";
        } else {
            return libDiscId.discid_get_toc_string(disc);
        }

    }

    @Override
    protected void finalize() {
        if (null != disc) {
            libDiscId.discid_free(disc);
            disc = null;
        }
    }
}

/**
 * Library function linking
 */
interface LibDiscId extends Library {

    Pointer discid_new();

    void discid_free(Pointer disc);

    boolean discid_read(Pointer disc, String drive);

    Pointer discid_get_id(Pointer disc);

    Pointer discid_get_freedb_id(Pointer disc);

    Pointer discid_get_submission_url(Pointer disc);

    Pointer discid_get_default_device();

    Pointer discid_get_error_msg(Pointer disc);

    String discid_get_toc_string(Pointer disc);
}