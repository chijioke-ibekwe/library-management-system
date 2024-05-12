package dev.chijiokeibekwe.librarymanagementsystem.common;

import dev.chijiokeibekwe.librarymanagementsystem.enums.ResponseStatus;

public record ResponseObject <T> (ResponseStatus status, String message, T data)
{
    //
}
