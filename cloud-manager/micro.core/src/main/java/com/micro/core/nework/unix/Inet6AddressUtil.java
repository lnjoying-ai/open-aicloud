package com.micro.core.nework.unix;

public class Inet6AddressUtil
{
    final static int INADDRSZ = 16;

    private final static int INT16SZ = 2;

    public static String numericToTextFormat(byte[] src) {
        StringBuilder sb = new StringBuilder(39);
        for (int i = 0; i < (INADDRSZ / INT16SZ); i++) {
            sb.append(Integer.toHexString(((src[i<<1]<<8) & 0xff00)
                    | (src[(i<<1)+1] & 0xff)));
            if (i < (INADDRSZ / INT16SZ) -1 ) {
                sb.append(":");
            }
        }
        return sb.toString();
    }
}
