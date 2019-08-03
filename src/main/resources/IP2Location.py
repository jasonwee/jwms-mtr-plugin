import sys
import struct
import socket

if sys.version < '3':
    def u(x):
        return x.decode('utf-8')
    def b(x):
        return str(x)
else:
    def u(x):
        if isinstance(x, bytes):
            return x.decode()
        return x
    def b(x):
        if isinstance(x, bytes):
            return x
        return x.encode('ascii')
        
# Windows version of Python does not provide it
#          for compatibility with older versions of Windows.
if not hasattr(socket, 'inet_pton'):
    def inet_pton(t, addr):
        import ctypes
        a = ctypes.WinDLL('ws2_32.dll')
        in_addr_p = ctypes.create_string_buffer(b(addr))
        if t == socket.AF_INET:
            out_addr_p = ctypes.create_string_buffer(4)
        elif t == socket.AF_INET6:
            out_addr_p = ctypes.create_string_buffer(16)
        n = a.inet_pton(t, in_addr_p, out_addr_p)
        if n == 0:
            raise ValueError('Invalid address')
        return out_addr_p.raw
    socket.inet_pton = inet_pton

class IP2LocationRecord:
    ''' IP2Location record with all fields from the database '''
    ip = None
    country_short = None
    country_long = None
    region = None
    city = None
    isp = None
    latitude = None
    longitude = None
    domain = None
    zipcode = None
    timezone = None
    netspeed = None
    idd_code = None
    area_code = None
    weather_code = None
    weather_name = None
    mcc = None
    mnc = None
    mobile_brand = None
    elevation = None
    usage_type = None

    def __str__(self):
        return str(self.__dict__)

    def __repr__(self):
        return repr(self.__dict__)

_COUNTRY_POSITION             = (0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2)
_REGION_POSITION              = (0, 0, 0, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3)
_CITY_POSITION                = (0, 0, 0, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4)
_ISP_POSITION                 = (0, 0, 3, 0, 5, 0, 7, 5, 7, 0, 8, 0, 9, 0, 9, 0, 9, 0, 9, 7, 9, 0, 9, 7, 9)
_LATITUDE_POSITION            = (0, 0, 0, 0, 0, 5, 5, 0, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5)
_LONGITUDE_POSITION           = (0, 0, 0, 0, 0, 6, 6, 0, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6)
_DOMAIN_POSITION              = (0, 0, 0, 0, 0, 0, 0, 6, 8, 0, 9, 0, 10,0, 10, 0, 10, 0, 10, 8, 10, 0, 10, 8, 10)
_ZIPCODE_POSITION             = (0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 7, 7, 7, 0, 7, 7, 7, 0, 7, 0, 7, 7, 7, 0, 7)
_TIMEZONE_POSITION            = (0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 8, 7, 8, 8, 8, 7, 8, 0, 8, 8, 8, 0, 8)
_NETSPEED_POSITION            = (0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 11,0, 11,8, 11, 0, 11, 0, 11, 0, 11)
_IDDCODE_POSITION             = (0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 12, 0, 12, 0, 12, 9, 12, 0, 12)
_AREACODE_POSITION            = (0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10 ,13 ,0, 13, 0, 13, 10, 13, 0, 13)
_WEATHERSTATIONCODE_POSITION  = (0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 14, 0, 14, 0, 14, 0, 14)
_WEATHERSTATIONNAME_POSITION  = (0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 15, 0, 15, 0, 15, 0, 15)
_MCC_POSITION                 = (0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 16, 0, 16, 9, 16)
_MNC_POSITION                 = (0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10,17, 0, 17, 10, 17)
_MOBILEBRAND_POSITION         = (0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11,18, 0, 18, 11, 18)
_ELEVATION_POSITION           = (0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11, 19, 0, 19)
_USAGETYPE_POSITION           = (0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 12, 20)

class IP2Location(object):
    ''' IP2Location database '''

    def __init__(self, filename=None):
        ''' Creates a database object and opens a file if filename is given
            
        '''
        if filename:
            self.open(filename)

    def __enter__(self):
        if not hasattr(self, '_f') or self._f.closed:
            raise ValueError("Cannot enter context with closed file")
        return self

    def __exit__(self, exc_type, exc_value, traceback):
        self.close()

    def open(self, filename):
        ''' Opens a database file '''
        # Ensure old file is closed before opening a new one
        self.close()

        self._f = open(filename, 'rb')
        self._dbtype = struct.unpack('B', self._f.read(1))[0]
        self._dbcolumn = struct.unpack('B', self._f.read(1))[0]
        self._dbyear = struct.unpack('B', self._f.read(1))[0]
        self._dbmonth = struct.unpack('B', self._f.read(1))[0]
        self._dbday = struct.unpack('B', self._f.read(1))[0]
        self._ipv4dbcount = struct.unpack('<I', self._f.read(4))[0]
        self._ipv4dbaddr = struct.unpack('<I', self._f.read(4))[0]
        self._ipv6dbcount = struct.unpack('<I', self._f.read(4))[0]
        self._ipv6dbaddr = struct.unpack('<I', self._f.read(4))[0]
        self._ipv4indexbaseaddr = struct.unpack('<I', self._f.read(4))[0]
        self._ipv6indexbaseaddr = struct.unpack('<I', self._f.read(4))[0]

    def close(self):
        if hasattr(self, '_f'):
            # If there is file close it.
            self._f.close()
            del self._f

    def get_country_short(self, ip):
        ''' Get country_short '''
        rec = self.get_all(ip)
        return rec and rec.country_short
    def get_country_long(self, ip):
        ''' Get country_long '''
        rec = self.get_all(ip)
        return rec and rec.country_long
    def get_region(self, ip):
        ''' Get region '''
        rec = self.get_all(ip)
        return rec and rec.region
    def get_city(self, ip):
        ''' Get city '''
        rec = self.get_all(ip)
        return rec and rec.city
    def get_isp(self, ip):
        ''' Get isp '''
        rec = self.get_all(ip)
        return rec and rec.isp
    def get_latitude(self, ip):
        ''' Get latitude '''
        rec = self.get_all(ip)
        return rec and rec.latitude
    def get_longitude(self, ip):
        ''' Get longitude '''
        rec = self.get_all(ip)
        return rec and rec.longitude
    def get_domain(self, ip):
        ''' Get domain '''
        rec = self.get_all(ip)
        return rec and rec.domain
    def get_zipcode(self, ip):
        ''' Get zipcode '''
        rec = self.get_all(ip)
        return rec and rec.zipcode
    def get_timezone(self, ip):
        ''' Get timezone '''
        rec = self.get_all(ip)
        return rec and rec.timezone
    def get_netspeed(self, ip):
        ''' Get netspeed '''
        rec = self.get_all(ip)
        return rec and rec.netspeed
    def get_idd_code(self, ip):
        ''' Get idd_code '''
        rec = self.get_all(ip)
        return rec and rec.idd_code
    def get_area_code(self, ip):
        ''' Get area_code '''
        rec = self.get_all(ip)
        return rec and rec.area_code
    def get_weather_code(self, ip):
        ''' Get weather_code '''
        rec = self.get_all(ip)
        return rec and rec.weather_code
    def get_weather_name(self, ip):
        ''' Get weather_name '''
        rec = self.get_all(ip)
        return rec and rec.weather_name
    def get_mcc(self, ip):
        ''' Get mcc '''
        rec = self.get_all(ip)
        return rec and rec.mcc
    def get_mnc(self, ip):
        ''' Get mnc '''
        rec = self.get_all(ip)
        return rec and rec.mnc
    def get_mobile_brand(self, ip):
        ''' Get mobile_brand '''
        rec = self.get_all(ip)
        return rec and rec.mobile_brand
    def get_elevation(self, ip):
        ''' Get elevation '''
        rec = self.get_all(ip)
        return rec and rec.elevation
    def get_usage_type(self, ip):
        ''' Get usage_type '''
        rec = self.get_all(ip)
        return rec and rec.usage_type

    def get_all(self, addr):
        ''' Get the whole record with all fields read from the file

            Arguments:

            addr: IPv4 or IPv6 address as a string
     
            Returns IP2LocationRecord or None if address not found in file
        '''
        return self._get_record(addr)

    def find(self, addr):
        ''' Get the whole record with all fields read from the file

            Arguments:

            addr: IPv4 or IPv6 address as a string
     
            Returns IP2LocationRecord or None if address not found in file
        '''
        return self._get_record(addr)

    def _reads(self, offset):
        self._f.seek(offset - 1)
        n = struct.unpack('B', self._f.read(1))[0]
        #return u(self._f.read(n))
        return self._f.read(n).decode('iso-8859-1').encode('utf-8')

    def _readi(self, offset):
        self._f.seek(offset - 1)
        return struct.unpack('<I', self._f.read(4))[0]

    def _readf(self, offset):
        self._f.seek(offset - 1)
        return struct.unpack('<f', self._f.read(4))[0]

    def _readip(self, offset, ipv):
        if ipv == 4:
            return self._readi(offset)
        elif ipv == 6:
            a, b, c, d = self._readi(offset), self._readi(offset + 4), self._readi(offset + 8), self._readi(offset + 12) 
            return (d << 96) | (c << 64) | (b << 32) | a

    def _readips(self, offset, ipv):
        if ipv == 4:
            return socket.inet_ntoa(struct.pack('!L', self._readi(offset)))
        elif ipv == 6:
            return str(self._readip(offset, ipv))

    def _read_record(self, mid, ipv):
        rec = IP2LocationRecord()

        if ipv == 4:
            off = 0
            baseaddr = self._ipv4dbaddr
        elif ipv == 6:
            off = 12
            baseaddr = self._ipv6dbaddr

        rec.ip = self._readips(baseaddr + (mid) * self._dbcolumn * 4, ipv)

        def calc_off(what, mid):
            return baseaddr + mid * (self._dbcolumn * 4 + off) + off + 4 * (what[self._dbtype]-1)

        if _COUNTRY_POSITION[self._dbtype] != 0:
            rec.country_short = self._reads(self._readi(calc_off(_COUNTRY_POSITION, mid)) + 1)
            rec.country_long =  self._reads(self._readi(calc_off(_COUNTRY_POSITION, mid)) + 4)

        if _REGION_POSITION[self._dbtype] != 0:
            rec.region = self._reads(self._readi(calc_off(_REGION_POSITION, mid)) + 1)
        if _CITY_POSITION[self._dbtype] != 0:
            rec.city = self._reads(self._readi(calc_off(_CITY_POSITION, mid)) + 1)
        if _ISP_POSITION[self._dbtype] != 0:
            rec.isp = self._reads(self._readi(calc_off(_ISP_POSITION, mid)) + 1)

        if _LATITUDE_POSITION[self._dbtype] != 0:
            rec.latitude = round(self._readf(calc_off(_LATITUDE_POSITION, mid)), 6)
        if _LONGITUDE_POSITION[self._dbtype] != 0:
            rec.longitude = round(self._readf(calc_off(_LONGITUDE_POSITION, mid)), 6)

        if _DOMAIN_POSITION[self._dbtype] != 0:
            rec.domain = self._reads(self._readi(calc_off(_DOMAIN_POSITION, mid)) + 1)

        if _ZIPCODE_POSITION[self._dbtype] != 0:
            rec.zipcode = self._reads(self._readi(calc_off(_ZIPCODE_POSITION, mid)) + 1)

        if _TIMEZONE_POSITION[self._dbtype] != 0:
            rec.timezone = self._reads(self._readi(calc_off(_TIMEZONE_POSITION, mid)) + 1)
                
        if _NETSPEED_POSITION[self._dbtype] != 0:
            rec.netspeed = self._reads(self._readi(calc_off(_NETSPEED_POSITION, mid)) + 1)

        if _IDDCODE_POSITION[self._dbtype] != 0:
            rec.idd_code = self._reads(self._readi(calc_off(_IDDCODE_POSITION, mid)) + 1)

        if _AREACODE_POSITION[self._dbtype] != 0:
            rec.area_code = self._reads(self._readi(calc_off(_AREACODE_POSITION, mid)) + 1)

        if _WEATHERSTATIONCODE_POSITION[self._dbtype] != 0:
            rec.weather_code = self._reads(self._readi(calc_off(_WEATHERSTATIONCODE_POSITION, mid)) + 1)

        if _WEATHERSTATIONNAME_POSITION[self._dbtype] != 0:
            rec.weather_name = self._reads(self._readi(calc_off(_WEATHERSTATIONNAME_POSITION, mid)) + 1)

        if _MCC_POSITION[self._dbtype] != 0:
            rec.mcc = self._reads(self._readi(calc_off(_MCC_POSITION, mid)) + 1)

        if _MNC_POSITION[self._dbtype] != 0:
            rec.mnc = self._reads(self._readi(calc_off(_MNC_POSITION, mid)) + 1)

        if _MOBILEBRAND_POSITION[self._dbtype] != 0:
            rec.mobile_brand = self._reads(self._readi(calc_off(_MOBILEBRAND_POSITION, mid)) + 1)
                
        if _ELEVATION_POSITION[self._dbtype] != 0:
            rec.elevation = self._reads(self._readi(calc_off(_ELEVATION_POSITION, mid)) + 1)

        if _USAGETYPE_POSITION[self._dbtype] != 0:
            rec.usage_type = self._reads(self._readi(calc_off(_USAGETYPE_POSITION, mid)) + 1)

        return rec

    def __iter__(self):
        low, high = 0, self._ipv4dbcount
        while low <= high:
            yield self._read_record(low, 4)
            low += 1

        low, high = 0, self._ipv6dbcount
        while low <= high:
            yield self._read_record(low, 6)
            low += 1

    def _parse_addr(self, addr): 
        ''' Parses address and returns IP version. Raises exception on invalid argument '''
        ipv = 0
        try:
            # socket.inet_pton(socket.AF_INET6, addr)
            a, b = struct.unpack('!QQ', socket.inet_pton(socket.AF_INET6, addr))
            ipnum = (a << 64) | b
            # Convert ::FFFF:x.y.z.y to IPv4
            if addr.lower().startswith('::ffff:'):
                try:
                    socket.inet_pton(socket.AF_INET, addr)
                    ipv = 4
                except:
                    # reformat ipv4 address in ipv6 
                    if ((ipnum >= 281470681743360) and (ipnum <= 281474976710655)):
                        ipv = 4
                        ipnum = ipnum - 281470681743360
                    else:
                        ipv = 6
            else:
                #reformat 6to4 address to ipv4 address 2002:: to 2002:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF
                if ((ipnum >= 42545680458834377588178886921629466624) and (ipnum <= 42550872755692912415807417417958686719)):
                    ipv = 4
                    ipnum = ipnum >> 80
                    ipnum = ipnum % 4294967296
                #reformat Teredo address to ipv4 address 2001:0000:: to 2001:0000:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:
                elif ((ipnum >= 42540488161975842760550356425300246528) and (ipnum <= 42540488241204005274814694018844196863)):
                    ipv = 4
                    ipnum = ~ ipnum
                    ipnum = ipnum % 4294967296
                else:
                    ipv = 6
        except:
            ipnum = struct.unpack('!L', socket.inet_pton(socket.AF_INET, addr))[0]
            # socket.inet_pton(socket.AF_INET, addr)
            ipv = 4
        return ipv
        
    def _get_record(self, ip):

        low = 0
        ipv = self._parse_addr(ip) 
        if ipv == 4:
            ipno = struct.unpack('!L', socket.inet_pton(socket.AF_INET, ip))[0]
            off = 0
            baseaddr = self._ipv4dbaddr
            high = self._ipv4dbcount
            if self._ipv4indexbaseaddr > 0:
                indexpos = ((ipno >> 16) << 3) + self._ipv4indexbaseaddr
                low = self._readi(indexpos)
                high = self._readi(indexpos + 4)

        elif ipv == 6:
            if self._ipv6dbcount == 0:
                raise ValueError('Please use IPv6 BIN file for IPv6 Address.')
            a, b = struct.unpack('!QQ', socket.inet_pton(socket.AF_INET6, ip))
            ipno = (a << 64) | b
            off = 12
            baseaddr = self._ipv6dbaddr
            high = self._ipv6dbcount
            if self._ipv6indexbaseaddr > 0:
                indexpos = ((ipno >> 112) << 3) + self._ipv6indexbaseaddr
                low = self._readi(indexpos)
                high = self._readi(indexpos + 4)

        while low <= high:
            mid = int((low + high) / 2)
            ipfrom = self._readip(baseaddr + (mid) * (self._dbcolumn * 4 + off), ipv)
            ipto = self._readip(baseaddr + (mid + 1) * (self._dbcolumn * 4 + off), ipv)

            if ipfrom <= ipno < ipto:
                return self._read_record(mid, ipv)
            else:
                if ipno < ipfrom:
                    high = mid - 1
                else:
                    low = mid + 1
