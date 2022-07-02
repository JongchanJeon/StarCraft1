import java.awt.Color;
import java.util.Vector;

import javax.swing.JLabel;

class CommandCenter extends TerranBuilding  implements Runnable{
	static int cost=400;
	static int gascost=0;
	static int buildtime=500;
	static JLabel currentCommand; //커멘더 정보 패널
	
	CommandCenter(StarUI ui){
		super(ui);
		name = "CommandCenter";
		flag=true;
		work=true;
		progress=0;
		energy=1500;
		con = new Vector();
		con.add("노는중");
		con.add("생산중");
		condi=(String)con.elementAt(0);
		go=new Thread(this);
		go.start();
		
		
	}
	public void run(){
		while(true){
			switch(job){
			case 0:
				flag=true;
				go.suspend();
				currentCommand.setText("생산중");
			break;
			case 1:
				flag=true;
				makeUnit();
				currentCommand.setText("노는중");
				break;
			}
		}
	}
	
	public void makeUnit(){
		if(ui.clan.getMineral()>=SCV.cost){
			
			if(ui.clan.getMaxunit()>ui.clan.getUnit()){ //최대유닛보다 현재 유닛이 적을 경우
				condi=(String)con.elementAt(1);
				ui.clan.setMineral(-SCV.cost);
				ui.U_mineral.setText(String.valueOf(ui.clan.getMineral()));
				ui.setTB(ui.clan.mainBVT);
				progressing(SCV.buildtime);
				ui.clan.workerVT.add(new SCV(ui));
				ui.clan.setUnit(ui.clan.workerVT.size());
				ui.U_unit.setText(String.valueOf(ui.clan.getUnit()));
				condi=(String)con.elementAt(0);
				
				if(ui.U_name.getText()==ui.clan.getWorkerName()) {
					ui.setTB(ui.clan.workerVT);
				}
				if(ui.U_name.getText()==ui.clan.getMainBName()) {
					ui.setTB(ui.clan.mainBVT);
			}
			else
				ui.setMessage("Need to SupplyDepot");
		}
		else 
			ui.setMessage("Not enough minerals");
			
		
		progress=0;
		job=0;
	}
}
}
