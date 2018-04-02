package net.amygdalum.util.stream;


public class Unescaper {

	private static final char UNICODE = 0;
	private static final char DEFAULT_ESCAPER = '\\';
	
	private char escaper;
	private char[] mappings;
	private boolean escaped;
	private int unicode;
	private char[] uc;
	private StringBuilder buffer;
	
	public Unescaper(char[] mappings) {
		this(DEFAULT_ESCAPER, mappings);
	}
	
	public Unescaper(char escaper, char[] mappings) {
		if (mappings.length % 2 != 0) {
			throw new IllegalArgumentException("character mapping must be a char array with length of a multiple of 2");
		}
		this.escaper = escaper;
		this.mappings = mappings;
		this.buffer = new StringBuilder();
		this.uc = new char[5];
		uc[0] = '#';
	}
	
	public static Unescaper defaultUnescaper(char... mappings) {
		return new Unescaper(mappings);
	}
	
	public static Unescaper customUnescaper(char escaper, char... mappings) {
		return new Unescaper(escaper, mappings);
	}
	
	public void consume(char ch) {
		if (unicode > 0) {
			uc[5 - unicode] = ch;
			unicode--;
			if (unicode == 0) {
				char ucch = (char) Integer.decode(new String(uc)).intValue();
				buffer.append(ucch);
			}
		} else if (escaped) {
			for (int i = 0; i < mappings.length; i+= 2) {
				if (mappings[i] == ch) {
					char unescaped = mappings[i +1 ];
					if (unescaped == UNICODE) {
						unicode = 4;
					} else {
						buffer.append(unescaped);
					}
					escaped = false;
					return;
				}
			}
			buffer.append(ch);
			escaped = false;
		} else if (ch == escaper){
			escaped = true;
		} else {
			buffer.append(ch);
		}
	}
	
	public void join(Unescaper other) {
		buffer.append(other.buffer);
	}
	
	@Override
	public String toString() {
		return buffer.toString();
	}
}
