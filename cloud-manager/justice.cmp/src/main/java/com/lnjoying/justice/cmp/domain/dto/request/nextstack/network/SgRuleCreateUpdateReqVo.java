package com.lnjoying.justice.cmp.domain.dto.request.nextstack.network;

import com.lnjoying.justice.cmp.domain.dto.AddressesRef;
import lombok.Data;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

@Data
public class SgRuleCreateUpdateReqVo
{
    private String name;

    @NotNull
    private short priority;

    @NotNull
    private short direction;

    @NotNull
    private short protocol;

    @NotNull
    private String port;

    @NotNull
    private short addressType;

    @NotNull
    private AddressesRef addressRef;

    @NotNull
    private short action;

    private String description;

    @AssertTrue(message = "port is invalid")
    private boolean isValidPortRange()
    {
        if (port.isEmpty() || "0".equals(port) || "all".equals(port)) return true;
        String[] ports = port.split(",");
        for (String portStr : ports) {
            if (portStr.contains("-")) {
                String[] range = portStr.split("-");
                if (range.length != 2) {
                    return false;
                }
                int start, end;
                try {
                    start = Integer.parseInt(range[0].trim());
                    end = Integer.parseInt(range[1].trim());
                } catch (NumberFormatException e) {
                    return false;
                }
                if (start < 0 || end < 0 || end > 65535 || start > end) {
                    return false;
                }
            } else {
                int portNumber;
                try {
                    portNumber = Integer.parseInt(portStr.trim());
                } catch (NumberFormatException e) {
                    return false;
                }
                if (portNumber < 0 || portNumber > 65535) {
                    return false;
                }
            }
        }
        return true;
    }
}
