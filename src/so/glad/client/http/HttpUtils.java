package so.glad.client.http;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Some utils for http low lever implementation
 * @author Palmtale
 * 2015-03-25
 */
public class HttpUtils {

	public static String fromQuotedString(String string){
		string = string.trim();
		if(string.startsWith("\"")&&string.endsWith("\"")){
			return string.substring(1, string.length()-1);
		}
		if(string.startsWith("'")&&string.endsWith("'")){
			return string.substring(1, string.length()-1);
		}
		return string;
	}
	
    public static String getQueryString(Map<String, Object> paramters, String charset) throws UnsupportedEncodingException {
        StringBuilder stringBuffer = new StringBuilder();
        for (Map.Entry<String, Object> entry : paramters.entrySet()) {
            String parameterName = entry.getKey();
            Object parameterValue = entry.getValue();
            parameterName = URLEncoder.encode(parameterName, charset);
            if (parameterValue instanceof List) {
                for (Object eachParameterValue : (List<?>) parameterValue) {
                    stringBuffer.append("&").append(parameterName).append("=");
                    stringBuffer.append(URLEncoder.encode(eachParameterValue.toString(), charset));
                }
            } else {
                stringBuffer.append("&").append(parameterName).append("=");
                stringBuffer.append(URLEncoder.encode(parameterValue.toString(), charset));
            }
        }

        return stringBuffer.toString().substring(1);
    }

    /**
     * get the timeout value from the value of header "Keep-Alive" in http response
     * It may be "30", "timeout=30"
     * @param keepAliveValue the value of header "Keep-Alive"
     * @return the timeout value based on second
     */
    public static int getKeepAliveTimeout(String keepAliveValue) {
        if(keepAliveValue == null) {
            return 0;
        }
        int timeoutIndex = keepAliveValue.indexOf("timeout=");
        if (timeoutIndex > -1) {
            int index = timeoutIndex + "timeout=".length();
            keepAliveValue = keepAliveValue.substring(index, keepAliveValue.indexOf(",", index));
        }
        keepAliveValue = keepAliveValue.trim();
        int aliveTimeout = 0;
        try {
            aliveTimeout = Integer.parseInt(keepAliveValue);
        } catch (NumberFormatException e) {
            Constant.LOG.warn("Format alive timeout value failed", e);
        }
        return aliveTimeout;
    }

    /**
     * transform the basic authorization username and password to a string value for Header "Authorization"
     * @param username the username
     * @param password the password
     * @return the string value for Header "Authorization"
     * @see
     */
    public static String getBasicAuthorization(String username, String password) {
        String authorizationValue = username + ":" + password;
        return "Basic " + new String(encodeBase64(authorizationValue.getBytes()));
    }

    /**
     * get the milliseconds value of the timeout value which was based on seconds
     * @param timeoutValue the timeout value
     * @return the milliseconds value
     */
    public static int getTimeoutMilliseconds(int timeoutValue) {
        return timeoutValue * 1000;
    }

    public static Map<String, Object> getParameters(URL url) {
        String queryString = url.getQuery();
        if (queryString != null) {
            Map<String, Object> parameters = new HashMap<String, Object>();
            for (String queryParameter : queryString.split("&")) {
                int equalIndex = queryParameter.indexOf("=");
                if(equalIndex!=-1){
                	String parameterName = queryParameter.substring(0, equalIndex);
                    String value = queryParameter.substring(equalIndex + 1);
                    if (parameters.get(parameterName) != null) {
                        List values = new ArrayList();
                        values.add(parameters.get(parameterName));
                        values.add(value);
                        parameters.put(parameterName, values);
                    } else {
                        parameters.put(parameterName, value);
                    }
                }
                
            }
            return parameters;
        }
        return null;
    }

    /**
     * Lookup length.
     */
    private static final int LOOKUPLENGTH = 64;

    /**
     * Used to calculate the number of bits in a byte.
     */
    private static final int EIGHTBIT = 8;

    /**
     * Used when encoding something which has fewer than 24 bits.
     */
    private static final int SIXTEENBIT = 16;

    /**
     * Used to determine how many bits data contains.
     */
    private static final int TWENTYFOURBITGROUP = 24;

    /**
     * Used to test the sign of a byte.
     */
    private static final int SIGN = -128;

    /**
     * Byte used to pad output.
     */
    private static final byte PAD = (byte) '=';

    private static byte[] lookUpBase64Alphabet = new byte[LOOKUPLENGTH];

    // Populating the lookup and character arrays
    static {
        for (int i = 0; i <= 25; i++) {
            lookUpBase64Alphabet[i] = (byte) ('A' + i);
        }

        for (int i = 26, j = 0; i <= 51; i++, j++) {
            lookUpBase64Alphabet[i] = (byte) ('a' + j);
        }

        for (int i = 52, j = 0; i <= 61; i++, j++) {
            lookUpBase64Alphabet[i] = (byte) ('0' + j);
        }

        lookUpBase64Alphabet[62] = (byte) '+';
        lookUpBase64Alphabet[63] = (byte) '/';
    }

    /**
     * Encodes binary data using the base64 algorithm, optionally
     * chunking the output into 76 character blocks.
     *
     * @param binaryData Array containing binary data to encode.
     * @return Base64-encoded data.
     */
    public static byte[] encodeBase64(byte[] binaryData) {
        int lengthDataBits = binaryData.length * EIGHTBIT;
        int fewerThan24bits = lengthDataBits % TWENTYFOURBITGROUP;
        int numberTriplets = lengthDataBits / TWENTYFOURBITGROUP;
        byte encodedData[];
        int encodedDataLength;

        if (fewerThan24bits != 0) {
            encodedDataLength = (numberTriplets + 1) * 4;
        } else {
            encodedDataLength = numberTriplets * 4;
        }

        encodedData = new byte[encodedDataLength];

        byte k, l, b1, b2, b3;

        int encodedIndex = 0;
        int dataIndex;
        int i;

        for (i = 0; i < numberTriplets; i++) {
            dataIndex = i * 3;
            b1 = binaryData[dataIndex];
            b2 = binaryData[dataIndex + 1];
            b3 = binaryData[dataIndex + 2];

            l = (byte) (b2 & 0x0f);
            k = (byte) (b1 & 0x03);

            byte val1 =
                ((b1 & SIGN) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
            byte val2 =
                ((b2 & SIGN) == 0) ? (byte) (b2 >> 4) : (byte) ((b2) >> 4 ^ 0xf0);
            byte val3 =
                ((b3 & SIGN) == 0) ? (byte) (b3 >> 6) : (byte) ((b3) >> 6 ^ 0xfc);

            encodedData[encodedIndex] = lookUpBase64Alphabet[val1];
            encodedData[encodedIndex + 1] =
                lookUpBase64Alphabet[val2 | (k << 4)];
            encodedData[encodedIndex + 2] =
                lookUpBase64Alphabet[(l << 2) | val3];
            encodedData[encodedIndex + 3] = lookUpBase64Alphabet[b3 & 0x3f];

            encodedIndex += 4;
        }

        dataIndex = i * 3;

        if (fewerThan24bits == EIGHTBIT) {
            b1 = binaryData[dataIndex];
            k = (byte) (b1 & 0x03);
            byte val1 =
                ((b1 & SIGN) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
            encodedData[encodedIndex] = lookUpBase64Alphabet[val1];
            encodedData[encodedIndex + 1] = lookUpBase64Alphabet[k << 4];
            encodedData[encodedIndex + 2] = PAD;
            encodedData[encodedIndex + 3] = PAD;
        } else if (fewerThan24bits == SIXTEENBIT) {

            b1 = binaryData[dataIndex];
            b2 = binaryData[dataIndex + 1];
            l = (byte) (b2 & 0x0f);
            k = (byte) (b1 & 0x03);

            byte val1 =
                ((b1 & SIGN) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
            byte val2 =
                ((b2 & SIGN) == 0) ? (byte) (b2 >> 4) : (byte) ((b2) >> 4 ^ 0xf0);

            encodedData[encodedIndex] = lookUpBase64Alphabet[val1];
            encodedData[encodedIndex + 1] =
                lookUpBase64Alphabet[val2 | (k << 4)];
            encodedData[encodedIndex + 2] = lookUpBase64Alphabet[l << 2];
            encodedData[encodedIndex + 3] = PAD;
        }

        return encodedData;
    }

}
