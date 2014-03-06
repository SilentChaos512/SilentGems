package silent.gems.core.util;

public class Color {

    public int r;
    public int g;
    public int b;
    
    public Color(int c) {
        
        r = c >> 16 & 255;
        g = c >> 8 & 255;
        b = c & 255;
    }
    
    public Color (int r, int g, int b) {
        
        this.r = r & 255;
        this.g = g & 255;
        this.b = b & 255;
    }
    
    @Override
    public String toString() {
        
        return String.format("(%d, %d, %d)", r, g, b);
    }
}