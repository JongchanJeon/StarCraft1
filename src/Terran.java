import java.util.*;

import javax.swing.ImageIcon;
class Terran extends Tribe {
	
	Terran(StarUI ui){
		
		super(ui);
		
		this.ui = ui;
		tribe = "Terran"; //���� �̸�
		//////////���� �� �ǹ� �̸�//////////
		mainbuildingName="CommandCenter";

		workerName="SCV";

		/////////������ �Ŀ�����//////
		workerPower=2;

		///////���� �� �ǹ� ������ ����/////
		workerEnergy=60;

		mainBEnergy=1500;

		//////////���� �� �ǹ� ������ ����/////
		mainBdesc = "Terran Command Center";

		workerdesc = "Terran SCV";

		///////////�ǹ��� ���� �̹���//////////////
		mainBIC = new ImageIcon("C:\\Users\\jjc63\\Desktop\\StarCraft\\StarCraft\\img/terranmainB.jpg");

		workerIC = new ImageIcon("C:\\Users\\jjc63\\Desktop\\StarCraft\\StarCraft\\img/terranworker.jpg");

		//////////�ǹ� �� ���� ���̽� �̹���/////////////
		buildingface = new ImageIcon("C:\\Users\\jjc63\\Desktop\\StarCraft\\StarCraft\\img/terranbuildingface.jpg");
		workerface =new ImageIcon("C:\\Users\\jjc63\\Desktop\\StarCraft\\StarCraft\\img/terranworkerface.jpg");

		
		
		///////////�ǹ� �� ���� ������(�ൿ)���� �ʱ�ȭ///////////	
		
		mainBVT = new ArrayList();

		workerVT = new ArrayList();

		
		mainBItem = new ArrayList<String>();
		mainBItem.add("SCV");
		

		workerItem = new ArrayList<String>();
		workerItem.add("���");
		workerItem.add("�̳׶�");
		workerItem.add("����");
		workerItem.add("CommandCenter");
		workerItem.add("�����ϱ�");
		

		
		mainBVT.add(new CommandCenter(ui)); //���� Ŀ�ǵ� �ǹ� �⺻ �ϳ�
		
		for(int i=0;i<4;i++){//���� SCV�⺻ 4��
			workerVT.add(new SCV(ui));
		}		
		setUpgrade(0);//���� ���׷��̵� �Ŀ� 0
		setUnit(workerVT.size());
		setBuilding(mainBVT.size());
		
	}
}
