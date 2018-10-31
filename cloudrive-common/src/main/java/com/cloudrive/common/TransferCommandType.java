package com.cloudrive.common;

import java.io.Serializable;

public enum TransferCommandType implements Serializable {
    RENAME,
    DELETE,
    GET,
    ISEXISTS,
    GETDIRLIST,
    FILEREQUEST
}
