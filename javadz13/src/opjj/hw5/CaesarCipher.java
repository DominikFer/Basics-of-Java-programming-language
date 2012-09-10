package opjj.hw5;

/**
 *	Program which enables encoding and decoding in {@link CaesarCipher} code. 
 */
public class CaesarCipher {

	private char[] alphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
	
	/**
	 * Encodes the message with the use of Caesar Cipher encoding.
	 * 
	 * @param input		Message you want to encode.
	 * @param shift		How much to shift the letters (to the right)?
	 * @return			Encoded message for <code>shift</code> number
	 * 					of shifts to right.
	 */
    public String encode(String input, int shift) {
    	String result = "";
    	
        for(int i = 0; i < input.length(); i++) {
        	char original = input.charAt(i);
        	
        	int position = -1;
        	for(int j = 0; j < alphabet.length; j++) {
        		if(alphabet[j] == original) {
        			position = j;
        			break;
        		}
        	}
        	
        	if(position != -1) {
        		position = (position + shift) % alphabet.length;
        		result += alphabet[position];
        	} else {
        		result += original;
        	}
        }
        
        return result;
    }

	/**
	 * Decodes the message with the use of Caesar Cipher encoding.
	 * 
	 * @param input		Message you want to decode.
	 * @param shift		How much to shift the letters (to the right)?
	 * @return			Decoded message for <code>shift</code> number
	 * 					of shifts to right.
	 */
    public String decode(String input, int shift) {
        return encode(input, 26 - shift);
    }

}
