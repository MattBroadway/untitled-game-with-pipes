/**
 * this class represents a logical Candle
 */
public class Candle {
    
    private final int type;
    private boolean lit;
    private int fuse;
    public static final int EMPTY = 0;
    public static final int NORMAL = 1;
    public static final int TRICK = 2;
    public static final int TNT = 3;
    public static final int KINDLE = 4;
    
    
    Candle(int type)
    {
        this.type = type;
        this.fuse = 0;
        if (type == KINDLE || type == EMPTY)
        {
            lit = false;
        }
        else
        {
            lit = true;
        }
    }
    
    Candle(int type, int fuse)
    {
        this.type = type;
        this.fuse = fuse;
        if (type == KINDLE || type == EMPTY)
        {
            lit = false;
        }
        else
        {
            lit = true;
        }
    }
    
    Candle(int type, boolean lit)
    {
        this.type = type;
        this.lit = lit;
        fuse = 0;
    }
    
    public boolean getLit()
    {
        return lit;
    }
    
    public String toString()
    {
        return "Type: " + type + ", Fuse: " + fuse;
    }
    
    public void setLit(boolean lit)
    {
        this.lit = lit;
    }
    
    @Override
    public int hashCode()
    {
          int hash = 10 * type;
          if(lit)
          {
              hash++;
          }
          return hash;
    }
    
    @Override
    public boolean equals(Object other)
    {
        return this.hashCode() == other.hashCode();
    }
    
    public void blowCandle()
    {
        switch(type)
        {
                case NORMAL:
                    if (lit)
                    {
                        lit = false;
                    }
                    break;
                case TRICK:
                    if (lit)
                    {
                        lit = false;
                    }
                    break;
                case TNT:
                    if (lit)
                    {
                        lit = false;
                    }
                    break;
                case KINDLE :
                    lit = !lit;
                    break;
                    
        }
    }
    
}
