import java.awt.event.ActionEvent;
import java.util.*;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;


class SCV extends TerranUnit implements Runnable{
	static int cost=50; //비용
	static int gascost=0; //가스비용
	static int buildtime = 70;
	static int attackspeed = 600;
	static JLabel wonjin0; // 현재 SCV 상태
	int info = 0;
	
	SCV(StarUI ui){
		super(ui);
		name = "SCV";
		energy=60;
		power=2;
		work=true;
		flag=true;
		progress=0;
		con = new Vector();
		con.addElement("노는중");
		con.addElement("미네랄");
		con.addElement("가스");
		con.addElement("커맨드");
		con.addElement("공격");
		condi=(String) con.elementAt(0);
		go=new Thread(this);
		go.start();
	}
		
	public void run(){
		while(true){

			switch(job){
			case 0:
				flag=true;
				go.suspend();
				break;
			case 1:
				flag=true;
				addMineral();
				break;
			case 2:
				flag=true;
				addGas();
				break;
			case 3:
				flag=true;
				makeMainB();
				break;
			case 4:
				flag=true;
				condi=(String)getCondition(5);
				int rand;
				ui.setTB(ui.clan.workerVT);
				while(true){ 
					if(ui.enemy.clan.workerVT.size()!=0){
						rand=random.nextInt(ui.enemy.clan.workerVT.size());						
						attack(power+ui.clan.getUpgrade(),attackspeed,ui.enemy.clan.workerVT,(StarObject)ui.enemy.clan.workerVT.get(rand));
						ui.enemy.clan.setUnit(ui.enemy.clan.workerVT.size());
						ui.enemy.U_unit.setText(String.valueOf(ui.enemy.clan.getUnit()));
						if(ui.enemy.U_name.getText()==ui.enemy.clan.getWorkerName()){ //보이는 화면이 일꾼일때
							ui.enemy.setTB(ui.enemy.clan.workerVT); // 제거
						}
						if(flag==false)
							
							break;
					}
					
					else if(ui.enemy.clan.mainBVT.size()!=0){
						rand=random.nextInt(ui.enemy.clan.mainBVT.size());
						attack(power+ui.clan.getUpgrade(),attackspeed,ui.enemy.clan.mainBVT,(StarObject)ui.enemy.clan.mainBVT.get(rand));
						ui.enemy.clan.setBuilding(ui.enemy.clan.mainBVT.size());
						if(ui.enemy.U_name.getText()==ui.enemy.clan.getMainBName()){ //보이는 화면이 일꾼일때
							ui.enemy.setTB(ui.enemy.clan.mainBVT); // 제거
						}
						if(flag==false)
							break;
					}
					if(ui.enemy.clan.getBuilding()==0||flag==false){ //건물이 다 업어지면
						job=0; //공격 중지
						condi=(String)getCondition(0);
						if(ui.enemy.U_name.getText()==ui.enemy.clan.getWorkerName())
							ui.setTB(ui.enemy.clan.workerVT);
						ui.endMessage("승리하였습니다.");
						ui.enemy.endMessage("패배하였습니다.");
						System.exit(0); //게임 종료
						break;
					}
				}
				if(flag==false)
					break;
				
				break;
			}
		}
	}
	
	public boolean getFlag(){
		return flag;
	}
	
	
	void addMineral(){
		condi=(String)getCondition(1);
		ui.setTB(ui.clan.workerVT); //상태정보 바꿈
		
		while(true){
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {}
			ui.clan.setMineral(8);;
			ui.U_mineral.setText(String.valueOf(ui.clan.getMineral()));
			resources += 8;
			
			if(flag==false){
				break;
			}
		}		
	}
	void addGas(){
		condi=(String)getCondition(2);
		ui.setTB(ui.clan.workerVT);
		
		while(true){
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {}
			ui.clan.setGas(8);
			ui.U_gas.setText(String.valueOf(ui.clan.getGas()));
			resources += 8;
			if(flag==false){
				break;
			}
		}		
	}
	void makeMainB(){
		if(ui.clan.getMineral()>=CommandCenter.cost){
			condi=(String)getCondition(3);
			ui.clan.setMineral(-CommandCenter.cost);
			ui.U_mineral.setText(String.valueOf(ui.clan.getMineral()));
			ui.setTB(ui.clan.workerVT);
			progressing(CommandCenter.buildtime);
			ui.clan.mainBVT.add(new CommandCenter(ui));
			ui.clan.setBuilding(ui.clan.mainBVT.size());
			condi=(String)getCondition(0);
			if(ui.U_name.getText()==ui.clan.getWorkerName())
				ui.setTB(ui.clan.workerVT);
			if(ui.U_name.getText()==ui.clan.getMainBName())
				ui.setTB(ui.clan.mainBVT);
		}
		else{
			ui.setMessage("Not enough minerals");
		}
		progress=0;
		job=0;
	}

	@Override
	public void attack(int power, int attackspeed, Vector vt, StarObject target) {
		// TODO Auto-generated method stub
		
	}


}
