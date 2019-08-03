package io.jenkins.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;

public class IP2Location {
    
    public static final List<Integer> COUNTRY_POSITION                       = Arrays.asList(0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2);
    public static final List<Integer> REGION_POSITION                          = Arrays.asList(0, 0, 0, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3);
    public static final List<Integer> CITY_POSITION                                = Arrays.asList(0, 0, 0, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4);
    public static final List<Integer> ISP_POSITION                                  = Arrays.asList(0, 0, 3, 0, 5, 0, 7, 5, 7, 0, 8, 0, 9, 0, 9, 0, 9, 0, 9, 7, 9, 0, 9, 7, 9);
    public static final List<Integer> LATITUDE_POSITION                        = Arrays.asList(0, 0, 0, 0, 0, 5, 5, 0, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5);
    public static final List<Integer> LONGITUDE_POSITION                    = Arrays.asList(0, 0, 0, 0, 0, 6, 6, 0, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6);
    public static final List<Integer> DOMAIN_POSITION                          = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 6, 8, 0, 9, 0, 10,0, 10, 0, 10, 0, 10, 8, 10, 0, 10, 8, 10);
    public static final List<Integer> ZIPCODE_POSITION                         = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 7, 7, 7, 0, 7, 7, 7, 0, 7, 0, 7, 7, 7, 0, 7);
    public static final List<Integer> TIMEZONE_POSITION                       = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 8, 7, 8, 8, 8, 7, 8, 0, 8, 8, 8, 0, 8);
    public static final List<Integer> NETSPEED_POSITION                       = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 11,0, 11,8, 11, 0, 11, 0, 11, 0, 11);
    public static final List<Integer> IDDCODE_POSITION                         = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 12, 0, 12, 0, 12, 9, 12, 0, 12);
    public static final List<Integer> AREACODE_POSITION                       = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10 ,13 ,0, 13, 0, 13, 10, 13, 0, 13);
    public static final List<Integer> WEATHERSTATIONCODE_POSITION  = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 14, 0, 14, 0, 14, 0, 14);
    public static final List<Integer> WEATHERSTATIONNAME_POSITION  = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 15, 0, 15, 0, 15, 0, 15);
    public static final List<Integer> MCC_POSITION                                  = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 16, 0, 16, 9, 16);
    public static final List<Integer> MNC_POSITION                                  = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10,17, 0, 17, 10, 17);
    public static final List<Integer> MOBILEBRAND_POSITION                = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11,18, 0, 18, 11, 18);
    public static final List<Integer> ELEVATION_POSITION                       = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11, 19, 0, 19);
    public static final List<Integer> USAGETYPE_POSITION                     = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 12, 20);
    
    private String filename;
    
    RandomAccessFile raf;
    private long dbType;
    private long dbColumn;
    private long dbYear;
    private long dbMonth;
    private long dbDay;
    
    private long ipv4dbcount;
    private long ipv4dbaddr;
    private long ipv6dbcount;
    private long ipv6dbaddr;
    private long ipv4indexbaseaddr;
    private long ipv6indexbaseaddr;
    
    public IP2Location(String filename) throws FileNotFoundException, IOException {
        this.filename = filename;
        
        raf = new RandomAccessFile(this.filename, "r");
        
        this.dbType = raf.read();
        this.dbColumn = raf.read();
        this.dbYear = raf.read();
        this.dbMonth = raf.read();
        this.dbDay = raf.read();
        
        byte[] _ipv4dbcount = new byte[4];
        raf.readFully(_ipv4dbcount);
        ipv4dbcount = ByteBuffer.wrap(_ipv4dbcount).order(ByteOrder.LITTLE_ENDIAN).getInt();
        
        byte[] _ipv4dbaddr = new byte[4];
        raf.readFully(_ipv4dbaddr);
        ipv4dbaddr = ByteBuffer.wrap(_ipv4dbaddr).order(ByteOrder.LITTLE_ENDIAN).getInt();
        
        byte[] _ipv6dbcount = new byte[4];
        raf.readFully(_ipv6dbcount);
        ipv6dbcount = ByteBuffer.wrap(_ipv6dbcount).order(ByteOrder.LITTLE_ENDIAN).getInt();
        
        byte[] _ipv6dbaddr = new byte[4];
        raf.readFully(_ipv6dbaddr);
        ipv6dbaddr = ByteBuffer.wrap(_ipv6dbaddr).order(ByteOrder.LITTLE_ENDIAN).getInt();
        
        byte[] _ipv4indexbaseaddr = new byte[4];
        raf.readFully(_ipv4indexbaseaddr);
        ipv4indexbaseaddr = ByteBuffer.wrap(_ipv4indexbaseaddr).order(ByteOrder.LITTLE_ENDIAN).getInt();
        
        byte[] _ipv6indexbaseaddr = new byte[4];
        raf.readFully(_ipv6indexbaseaddr);
        ipv6indexbaseaddr = ByteBuffer.wrap(_ipv6indexbaseaddr).order(ByteOrder.LITTLE_ENDIAN).getInt();

    }

    private IP2LocationRecord getRecord(String addr) throws UnknownHostException, IOException {
        long low = 0;
        long high = 0;
        int ipv = parseAddr(addr);
        long baseaddr = 0;
        long off = 0;
        
        BigInteger ipno = new BigInteger("0");
        
        if (ipv == 4) {
            byte[] addrByte = InetAddress.getByName(addr).getAddress();
            ipno = BigInteger.valueOf(ByteBuffer.wrap(addrByte).order(ByteOrder.BIG_ENDIAN).getInt());
            off = 0;
            baseaddr = ipv4dbaddr;
            high = ipv4dbcount;
            if (ipv4indexbaseaddr > 0) {
                long indexPos = ((ipno.longValue() >> 16) << 3) + ipv4indexbaseaddr;
                low = readi(indexPos);
                high = readi(indexPos + 4);
            }
        } else if (ipv == 6) {
            if (ipv6dbcount == 0)
                throw new RuntimeException("Please use IPv6 BIN file for IPv6 Address.");

            byte[] addrByte = Inet6Address.getByName(addr).getAddress();
            ipno = new BigInteger(addrByte);
            
            off = 12;
            
            baseaddr = ipv6dbaddr;
            high = ipv6dbcount;

            
            if (ipv6indexbaseaddr > 0 ) {
                BigInteger indexpos = ipno.shiftRight(112).shiftLeft(3).add(new BigInteger(String.valueOf(ipv6indexbaseaddr)));
                low = readi(indexpos.longValue());
                high = readi(indexpos.longValue() + 4);
            }
        }
        
        while (low <= high) {
            long mid = (low + high) / 2;
            BigInteger ipfrom = readip(baseaddr + (mid) * (dbColumn * 4 + off), ipv);
            BigInteger ipto = readip(baseaddr + (mid + 1) * (dbColumn * 4 + off), ipv);

            if ( (ipfrom.compareTo(ipno) == -1 || ipfrom.compareTo(ipno) == 0) && ipno.compareTo(ipto) == -1) {
                return readRecord(mid, ipv);
            } else {
                if (ipno.compareTo(ipfrom) == -1)
                    high = mid - 1;
                else
                    low = mid + 1;
            }
        }
        
        return null;
    }
    
    public static long round( double d, int digits  ) {
        BigDecimal bd = new BigDecimal(d).setScale(digits, RoundingMode.HALF_UP);
        return bd.longValue();
    }

    private IP2LocationRecord readRecord(long mid, int ipv) throws IOException {
        IP2LocationRecord rec = new IP2LocationRecord();
        
        int off = 0;
        long baseaddr = 0;
        
        if (ipv == 4) {
            off = 0;
            baseaddr = ipv4dbaddr;
        } else if (ipv == 6) {
            off = 12;
            baseaddr = ipv6dbaddr;
        }

        rec.setIp( readips(baseaddr + (mid) * dbColumn * 4, ipv) );
        
        if (COUNTRY_POSITION.get((int)dbType) != 0) {
            rec.setCountry_short( reads( readi(calc_off(baseaddr, off, COUNTRY_POSITION, mid)) + 1 ).trim() );
            rec.setCountry_long( reads( readi(calc_off(baseaddr, off, COUNTRY_POSITION, mid)) + 4 ).trim() );
        }
        
        if (REGION_POSITION.get((int)dbType) != 0) {
            rec.setRegion(reads(readi(calc_off(baseaddr, off, REGION_POSITION, mid)) + 1).trim());
        }
        
        if (CITY_POSITION.get((int)dbType) != 0) {
            rec.setCity(reads( readi(calc_off(baseaddr, off, CITY_POSITION, mid)) + 1).trim());
        }
        
        if (ISP_POSITION.get((int)dbType) != 0) {
            rec.setIsp(reads(readi(calc_off(baseaddr, off, ISP_POSITION, mid)) + 1).trim());
        }
        
        if (LATITUDE_POSITION.get((int)dbType) != 0) {
            rec.setLatitude(String.valueOf(round(readf(calc_off(baseaddr, off, LATITUDE_POSITION, mid)), 6)));
        }
        
        if (LONGITUDE_POSITION.get((int)dbType) != 0) {
            rec.setLongitude(String.valueOf(round(readf(calc_off(baseaddr, off, LONGITUDE_POSITION, mid)), 6)));
        }

        if (DOMAIN_POSITION.get((int)dbType) != 0) {
            rec.setDomain(reads(readi(calc_off(baseaddr, off, DOMAIN_POSITION, mid)) + 1));
        }
        
        if (ZIPCODE_POSITION.get((int)dbType) != 0) {
            rec.setZipcode(reads(readi(calc_off(baseaddr, off, ZIPCODE_POSITION, mid)) + 1));
        }
        
        if (TIMEZONE_POSITION.get((int)dbType) != 0) {
            rec.setTimezone(reads(readi(calc_off(baseaddr, off, TIMEZONE_POSITION, mid)) +1));
        }
        
        if (NETSPEED_POSITION.get((int)dbType) != 0) {
            rec.setNetspeed(reads(readi(calc_off(baseaddr, off, NETSPEED_POSITION, mid)) +1));
        }
        
        if (IDDCODE_POSITION.get((int)dbType) != 0) {
            rec.setIdd_code(reads(readi(calc_off(baseaddr, off, IDDCODE_POSITION, mid)) +1));
        }
        
        if (AREACODE_POSITION.get((int)dbType) != 0) {
            rec.setArea_code(reads(readi(calc_off(baseaddr, off, AREACODE_POSITION, mid)) +1));
        }
        
        if (WEATHERSTATIONCODE_POSITION.get((int)dbType) != 0) {
            rec.setWeather_code(reads(readi(calc_off(baseaddr, off, WEATHERSTATIONCODE_POSITION, mid)) +1));
        }
        
        if (WEATHERSTATIONNAME_POSITION.get((int)dbType) != 0) {
            rec.setWeather_name(reads(readi(calc_off(baseaddr, off, WEATHERSTATIONNAME_POSITION, mid)) +1));
        }
        
        if (MCC_POSITION.get((int)dbType) != 0) {
            rec.setMcc(reads(readi(calc_off(baseaddr, off, MCC_POSITION, mid)) +1));
        }
        
        if (MNC_POSITION.get((int)dbType) != 0) {
            rec.setMnc(reads(readi(calc_off(baseaddr, off, MNC_POSITION, mid)) +1));
        }
        
        if (MOBILEBRAND_POSITION.get((int)dbType) != 0) {
            rec.setMobile_brand(reads(readi(calc_off(baseaddr, off, MOBILEBRAND_POSITION, mid)) +1));
        }
        
        if (ELEVATION_POSITION.get((int)dbType) != 0) {
            rec.setElevation(reads(readi(calc_off(baseaddr, off, ELEVATION_POSITION, mid)) +1));
        }
        
        if (USAGETYPE_POSITION.get((int)dbType) != 0) {
            rec.setUsage_type(reads(readi(calc_off(baseaddr, off, USAGETYPE_POSITION, mid)) +1));
        }

        return rec;
    }
    

    private long calc_off(long baseaddr, int off, List<Integer> what, long mid) {
        return baseaddr + mid * (dbColumn * 4 + off) + off + 4 * (what.get((int)dbType) - 1);
    }

    private String readips(long offset, int ipv) throws IOException {
        
        if (ipv == 4) {
            byte[] bytes = BigInteger.valueOf(readi(offset)).toByteArray();
            InetAddress address = InetAddress.getByAddress(bytes);
            return address.getHostAddress();
        } else if (ipv == 6) {
            return String.valueOf(readip(offset, ipv));
        }
        
        throw new RuntimeException("invalid ip");
    }

    private BigInteger readip(long offset, int ipv) throws IOException {
        if (ipv == 4) {
            BigInteger res = BigInteger.valueOf(readi(offset));
            return res;
        } else if (ipv == 6) {
            long a = readi(offset);
            long b = readi(offset + 4);
            long c = readi(offset + 8);
            long d = readi(offset + 12);
            
            BigInteger a1  = BigInteger.valueOf(a);
            BigInteger b1  = BigInteger.valueOf(b).shiftLeft(32);
            BigInteger c1  = BigInteger.valueOf(c).shiftLeft(64);
            BigInteger d1  = BigInteger.valueOf(d).shiftLeft(96);
            
            BigInteger res = d1.or(c1).or(b1).or(a1);
            return res;
        }
        throw new RuntimeException("invalid offset and ipv");
    }
    
    private String reads(long offset) throws IOException {
        raf.seek(offset - 1);
        int n = raf.read();
        byte[] b = new byte[100000];
        raf.read(b, 0, n);
        return new String(b);
    }
    
    private long readi(long offset) throws IOException {
        raf.seek(offset - 1);
        byte[] custom = new byte[4];
        raf.readFully(custom);
        long result = ByteBuffer.wrap(custom).order(ByteOrder.LITTLE_ENDIAN).getInt();

        if (result < 0) {
            result = result & 0x00000000ffffffffL;
        }
        return result;
    }
    
    private float readf(long offset) throws IOException {
        raf.seek(offset - 1);
        byte[] custom = new byte[4];
        raf.readFully(custom);
        return ByteBuffer.wrap(custom).order(ByteOrder.LITTLE_ENDIAN).getFloat();
    }
    
    private int parseAddr(String addr) {
        int ipv = 0;
        
        try {
            InetAddress inet = InetAddress.getByName(addr);
            if (inet instanceof Inet4Address) {
                ipv = 4;
            } else if (inet instanceof Inet6Address) {
                ipv = 6;
            }
        } catch (UnknownHostException e) {
            ipv = 4 ;
        }
        
        return ipv;
    }

    public IP2LocationRecord getAll(String addr) throws UnknownHostException, IOException {
        return getRecord(addr);
    }
    
    public IP2LocationRecord find(String addr) throws UnknownHostException, IOException {
        return getRecord(addr);
    }

}
