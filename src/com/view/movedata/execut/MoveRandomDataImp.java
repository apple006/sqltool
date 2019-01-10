package com.view.movedata.execut;

import java.sql.SQLException;

import com.view.movedata.IQuery;
import com.view.movedata.exp.entity.OddColumnMsg;
import com.view.movedata.exp.entity.SubmitDataGroup;
import com.view.tool.HelpProperties;
import com.view.tool.HelpRslutToSql;

public class MoveRandomDataImp  implements  IQuery{
		/**
		 * 每次从数据库中取出数据大小
		 */
		private int fetchSize = 5000;
		/**
		 * 块缓存数据大小
		 */
		private int cathSize = 1000;
		private MoveDataCtr ctr;
		public MoveRandomDataImp(MoveDataCtr ctr){
			cathSize = new Integer( HelpProperties.GetValueByKey("keyvalue.properties", "cathSize"));
			this.ctr = ctr;
		}
		@Override
		public void run() {
			try {
				ctr.setStartDate();

				OddColumnMsg[] oddColumns = ctr.getMoveDataMsg().getOddColumns();
				int rowCount = ctr.getMoveDataMsg().getRowCount();
				int type[] = new int[oddColumns.length];
				SubmitDataGroup group = new SubmitDataGroup(type);
				int countRow=0;
				for (int i = 0; i < rowCount; i++) {
					group.addRow(HelpRslutToSql.getArrayValue(null,ctr.getMoveDataMsg()));
					countRow++;
					if(countRow>=cathSize){
						countRow=0;
						ctr.getCath().push(group);
						group = new SubmitDataGroup(type);
					}

				}
				ctr.getCath().push(group);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
			finally{
				SubmitDataGroup group = new SubmitDataGroup(null);
				ctr.getCath().push(group);

			}
		}


		
		
}
