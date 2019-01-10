package com.dao.imp;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.dao.ITemplate;
import com.dao.entity.Column;
import com.dao.entity.Table;
import com.ui.extensible.Cell;

import redis.clients.jedis.Jedis;


public class RedisTemplate implements ITemplate{
	

	public  final String DEL         = "DEL";
	public  final String DUMP        = "DUMP";
	public  final String EXISTS      ="EXISTS";
	public  final String EXPIRE      ="EXPIRE";
	public  final String EXPIREAT    ="EXPIREAT";
	public  final String KEYS        ="KEYS";
	public  final String MIGRATE     ="MIGRATE";
	public  final String MOVE        ="MOVE";
	public  final String OBJECT      ="OBJECT";
	public  final String PERSIST     ="PERSIST";
	public  final String PEXPIRE     ="PEXPIRE";
	public  final String PEXPIREAT   ="PEXPIREAT";
	public  final String PTTL        ="PTTL";
	public  final String RANDOMKEY   ="RANDOMKEY";
	public  final String RENAME      ="RENAME";
	public  final String RENAMENX    ="RENAMENX";
	public  final String RESTORE     ="RESTORE";
	public  final String SORT        ="SORT";
	public  final String TTL         ="TTL";
	public  final String TYPE        ="TYPE";
	public  final String SCAN        ="SCAN";
	
	
	
	public  final String APPEND        ="APPEND";
	public  final String SET        ="SET";
	public  final String GET        ="GET";
	public  final String STRLEN        ="STRLEN";
	
	
	
	
	
	
	
	public  final String HLEN        ="HLEN";
	public  final String HSET        ="HSET";
	public  final String HVALS        ="HVALS";
	public  final String HGET        ="HGET";
	public  final String HKEYS        ="HKEYS";
	
	
	
	public  final String LPUSH        ="LPUSH";
	public  final String LLEN        ="LLEN";
	public  final String LRANGE        ="LRANGE";
	public  final String RPOP        ="RPOP";
	public  final String RPOPLPUSH        ="RPOPLPUSH";
	
	
	
	public  final String SADD        ="SADD";
	public  final String SCARD        ="SCARD";
	public  final String SREM        ="SREM";
	public  final String SINTER        ="SINTER";
	public String getDBName() {
		return REDIS;
	}

	public Table execute(String[] sql,Jedis jedis){
		String arr[] =  new String[sql.length-1];
		for (int i = 1; i < sql.length; i++) {
			arr[i-1] = sql[i];
		}
		Table table = new Table();
//		String columnCode = metaData.getColumnName(i);
//		int t = metaData.getColumnType(i);
//		int nullable = metaData.isNullable(i);
//		column.setNumber(0);
//		table.addColumn(column);
//		while(iterator.hasNext()){
//			String next = iterator.next();
//			Cell cell = new Cell(next);
//			table.addColumnValue(0, cell);
//		}
		switch (sql[0].toUpperCase()) {
		case DEL:
			Long del2 = jedis.del(arr);
			Column column = new Column();
			column.setColumnCode("del-length");
			column.setNumber(0);
			table.addColumn(column);
			Cell cell = new Cell(del2);
			table.addColumnValue(0, cell);
			break;
		case DUMP:
			jedis.dump(arr[0]);
			
			break;
		case EXISTS:
			Long exists = jedis.exists(arr);
			Column existsColumn = new Column();
			existsColumn.setColumnCode("说明");
			existsColumn.setNumber(0);
			Column existsColumn1 = new Column();
			existsColumn1.setColumnCode("EXISTS");
			existsColumn1.setNumber(1);
			table.addColumn(existsColumn);
			table.addColumn(existsColumn1);
			Cell existsCell = new Cell("存在");
			if(exists==0L){
				existsCell = new Cell("不存在");
			}
			table.addColumnValue(0, existsCell);
			table.addColumnValue(1, new Cell(exists));
			break;
		case EXPIRE :
			Long expire = jedis.expire(arr[0], Integer.valueOf(arr[1]));
			Column expireColumn = new Column();
			expireColumn.setColumnCode("expire-time");
			expireColumn.setNumber(0);
			table.addColumn(expireColumn);
			Cell expireCell = new Cell(expire);
			table.addColumnValue(0, expireCell);
			break;
		case EXPIREAT:
			jedis.expireAt(arr[0], Integer.valueOf(arr[1]));
			break;
		case KEYS:
			Set<String> keys = jedis.keys(arr[0]);
			Iterator<String> iterator = keys.iterator();
			table = new Table();
			Column keyscolumn = new Column();
			keyscolumn.setColumnCode("keys");
			keyscolumn.setNumber(0);
			table.addColumn(keyscolumn);
			while(iterator.hasNext()){
				String next = iterator.next();
				table.addColumnValue(0, new Cell(next));
			}
			break;
		case PTTL:
			Long pttl2 = jedis.pttl(arr[0]);
			Column ttl2column = new Column();
			ttl2column.setColumnCode("expire-time");
			ttl2column.setNumber(0);
			table.addColumn(ttl2column);
			table.addColumnValue(0, new Cell(pttl2));
			break;
		case TTL:
			Long ttl = jedis.ttl(arr[0]);
			Column ttlcolumn = new Column();
			ttlcolumn.setColumnCode("expire-time");
			ttlcolumn.setNumber(0);
			table.addColumn(ttlcolumn);
			table.addColumnValue(0, new Cell(ttl));
			break;
		case RENAME:
			String rename = jedis.rename(arr[0], arr[1]);
			Column renamecolumn = new Column();
			renamecolumn.setColumnCode("rename");
			renamecolumn.setNumber(0);
			table.addColumn(renamecolumn);
			table.addColumnValue(0, new Cell(rename));
			break;
		case TYPE:
			String type = jedis.type(arr[0]);
			Column typecolumn = new Column();
			typecolumn.setColumnCode("rename");
			typecolumn.setNumber(0);
			table.addColumn(typecolumn);
			table.addColumnValue(0, new Cell(type));
			break;
		case SCAN :
			jedis.scan(arr[0]);
			break;
			
			
			
			
			
			
			
			
			
		case APPEND :
			Long append = jedis.append(arr[0],arr[1]);
			Column appendcolumn = new Column();
			appendcolumn.setColumnCode("str-length");
			appendcolumn.setNumber(0);
			table.addColumn(appendcolumn);
			table.addColumnValue(0, new Cell(append));
			break;
		case SET :
			String set = jedis.set(arr[0],arr[1]);
			Column setcolumn = new Column();
			setcolumn.setColumnCode("result");
			setcolumn.setNumber(0);
			table.addColumn(setcolumn);
			table.addColumnValue(0, new Cell(set));
			break;
		case GET :
			String get = jedis.get(arr[0]);
			Column getcolumn = new Column();
			getcolumn.setColumnCode("result");
			getcolumn.setNumber(0);
			table.addColumn(getcolumn);
			table.addColumnValue(0, new Cell(get));
			break;
		case STRLEN :
			Long strlen = jedis.strlen(arr[0]);
			Column strlencolumn = new Column();
			strlencolumn.setColumnCode("str-length");
			strlencolumn.setNumber(0);
			table.addColumn(strlencolumn);
			table.addColumnValue(0, new Cell(strlen));
			break;
			
			
			
			
			
			
			
			
			
		case HSET :
			Long hset = jedis.hset((arr[0]), (arr[1]), (arr[2]));
			Column hsetcolumn = new Column();
			hsetcolumn.setColumnCode("hash-length");
			hsetcolumn.setNumber(0);
			table.addColumn(hsetcolumn);
			table.addColumnValue(0, new Cell(hset));
			break;
		case HLEN :
			Long hlen = jedis.hlen((arr[0]));
			Column hlencolumn = new Column();
			hlencolumn.setColumnCode("hash-length");
			hlencolumn.setNumber(0);
			table.addColumn(hlencolumn);
			table.addColumnValue(0, new Cell(hlen));
			break;
		case HVALS  :
			List<String> hvals = jedis.hvals((arr[0]));
			Column hvalscolumn = new Column();
			hvalscolumn.setColumnCode("hash-values");
			hvalscolumn.setNumber(0);
			table.addColumn(hvalscolumn);
			int size = hvals.size();
			for (int i = 0; i < size; i++) {
				table.addColumnValue(0,new Cell(hvals.get(i)));
			}
			break;
		case HGET  :
			String harr[] =  new String[arr.length-1];
 
			for (int i = 0; i < harr.length; i++) {
				harr[i] = arr[i+1];
			}
			String hget = jedis.hget(arr[0], arr[1]);
			Column hgetcolumn = new Column();
			hgetcolumn.setColumnCode("hget");
			hgetcolumn.setNumber(0);
			table.addColumn(hgetcolumn);
			table.addColumnValue(0,new Cell(hget));
			break;
		case HKEYS  :
			Set<String> hkeys = jedis.hkeys(arr[0]);
			Iterator<String> hkeysiterator2 = hkeys.iterator();
			Column hkeyscolumn = new Column();
			hkeyscolumn.setColumnCode("hkeys");
			hkeyscolumn.setNumber(0);
			table.addColumn(hkeyscolumn);
			while(hkeysiterator2.hasNext()){
				table.addColumnValue(0, new Cell(hkeysiterator2.next()));
				
			}
			
			break;
			
		case LPUSH  :
			Long lpush = jedis.lpush((arr[0]),arr[1]);
			Column lpushcolumn = new Column();
			lpushcolumn.setColumnCode("list-length");
			lpushcolumn.setNumber(0);
			table.addColumn(lpushcolumn);
			table.addColumnValue(0, new Cell(lpush));
			break;
		case LLEN  :
			Long llen = jedis.llen((arr[0]));
			Column llencolumn = new Column();
			llencolumn.setColumnCode("list-length");
			llencolumn.setNumber(0);
			table.addColumn(llencolumn);
			table.addColumnValue(0, new Cell(llen));
			break;
		case LRANGE  :
			List<String> lrange = jedis.lrange(arr[0],Integer.valueOf(arr[1]),Integer.valueOf(arr[2]));
			Column lrangecolumn = new Column();
			lrangecolumn.setColumnCode("list-values");
			lrangecolumn.setNumber(0);
			table.addColumn(lrangecolumn);
			int size1 = lrange.size();
			for (int i = 0; i < size1; i++) {
				table.addColumnValue(0,new Cell(lrange.get(i)) );
			}
			break;
		case RPOP  :
			String rpop = jedis.rpop(arr[0]);
			Column rpopcolumn = new Column();
			rpopcolumn.setColumnCode("list-rpop-value");
			rpopcolumn.setNumber(0);
			table.addColumn(rpopcolumn);
			table.addColumnValue(0, new Cell(rpop));
			break;
		case RPOPLPUSH  :
			String rpoplpush = jedis.rpoplpush(arr[0],arr[1]);
			Column rpoplpushcolumn = new Column();
			rpoplpushcolumn.setColumnCode("list-rpoplpush-value");
			rpoplpushcolumn.setNumber(0);
			table.addColumn(rpoplpushcolumn);
			table.addColumnValue(0, new Cell(rpoplpush));
			break;
			
			
			
		case SADD  :
			Long sadd = jedis.sadd(arr[0],arr[1]);
			Column saddcolumn = new Column();
			saddcolumn.setColumnCode("set-sadd-size");
			saddcolumn.setNumber(0);
			table.addColumn(saddcolumn);
			table.addColumnValue(0, new Cell(sadd));
			break;
		case SCARD  :
			Long scard = jedis.scard(arr[0]);
			Column scardcolumn = new Column();
			scardcolumn.setColumnCode("set-length");
			scardcolumn.setNumber(0);
			table.addColumn(scardcolumn);
			table.addColumnValue(0, new Cell(scard));
			break;
		case SREM  :
			Long srem = jedis.srem(arr[0],arr[1]);
			Column sremcolumn = new Column();
			sremcolumn.setColumnCode("set-srem-size");
			sremcolumn.setNumber(0);
			table.addColumn(sremcolumn);
			table.addColumnValue(0, new Cell(srem));
			break;
		case SINTER  :
			 Set<String> sinter2 = jedis.sinter(arr);
			Iterator<String> iterator2 = sinter2.iterator();
			while(iterator2.hasNext()){
				Column sintercolumn = new Column();
				sintercolumn.setColumnCode("set-srem-size");
				sintercolumn.setNumber(0);
				table.addColumn(sintercolumn);
				table.addColumnValue(0, new Cell(iterator2.next()));
				
			}
			break;
		default:
			break;
		}
		return table;
	}
}
