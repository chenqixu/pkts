package io.pkts.diameter.avp;

import io.pkts.diameter.avp.impl.DiameterUnsigned32Avp;
import io.pkts.diameter.avp.type.Unsigned32;

/**
 * TODO：ResultCode类丢失，瞎写了一个
 *
 * @author chenqixu
 */
public interface ResultCode extends Avp<Unsigned32> {
    int CODE = 999;// 瞎写

    // 瞎写
    static ResultCode parse(final FramedAvp raw) {
        if (CODE != raw.getCode()) {
            throw new AvpParseException("AVP Code mismatch - unable to parse the AVP into a " + ResultCode.class.getName());
        }

        return new DefaultResultCode(raw);
    }

    // 瞎写
    class DefaultResultCode extends DiameterUnsigned32Avp implements ResultCode {
        private DefaultResultCode(final FramedAvp raw) {
            super(raw);
        }
    }

    // 瞎写
    enum ResultCodeEnum {
        DIAMETER_SUCCESS_2001;
    }
}
