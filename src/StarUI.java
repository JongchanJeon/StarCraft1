import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.table.*;
import javax.swing.border.*;


class StarUI extends JFrame implements Runnable, MouseListener, ActionListener, ListSelectionListener
{
	StarUI enemy;//적을 가리킴
	static JPanel panel; //전체적인 패널
	JPanel jPanel1;//미네랄정보 패널
	JPanel jPanel2;//건물 정보 패널
	JPanel[] jPanel3 = new JPanel[10];//유닛 정보 패널
	JPanel jPanel5;//선택 정보 패널
	JLabel U_mineralLB; //미네랄 아이콘
	JLabel U_gasLB; //가스아이콘
	JLabel U_unitLB; //유닛정보 아이콘
	static JLabel[] wonjin = new JLabel[10]; // 유닛 상태확인
	
	
	JLabel[] U_BU = new JLabel[11]; //건물 및 유닛 라벨
			
	JLabel U_nameLB; //이름
	JLabel U_powerLB; //공격력
	JLabel U_countLB; //수량
	JLabel U_energyLB;//에너지
	
	JLabel U_desc; //선택 간략 설명
	JLabel U_progressingLB; //progressing라벨
	JLabel U_tribe; // 선택 종족
	JLabel U_mineral; // 미네랄
	JLabel U_gas; // 가스
	JLabel U_unit; // 유닛
	JLabel U_slush; // "/"
	JLabel U_maxunit; // 최대 만들수 있는 유닛
	
	JLabel U_name; //선택 이름
	JLabel U_power; // 선택 파워
	JLabel U_cnt; // 선택 수량
	JLabel U_face; // 선택 페이스(아이콘)
	JLabel U_message; //메세지 출력 라벨
	JProgressBar U_energyPB; //에너지 바
	JProgressBar U_progressPB; //진행상태 바

	Vector U_infoTB_model_vector; //테이블 컬럼
	DefaultTableModel U_infoTB_DTM; //디폴트 테이블 모델
	JTable U_infoTB; // 테이블 선택 객체 정보
	JScrollPane U_infoTB_scroll; //테이블 스크롤바
	JButton U_selectBT; // 수행 버튼
	JComboBox cb; //선택된 객체 할수 있는일
	
	Object[] DefOB; //테이블 값 변경시켜주기위한 오브젝트

	Tribe clan; //선택된 종족 참조변수
	Thread progressing; //진행상태 바와 에너지 바를 갱신해주기위한 쓰레드
	int rowcnt; //선택된 행 변수(테이블)
	int job; // 쓰레드의 작업 컨트롤 변수
	public StarUI(int tribe, String User)
	{
		switch(tribe){
		case 0:
			setClan(new Terran(this));
			break;		
		}
		
		UIManager.put("swing.boldMetal", Boolean.FALSE);

		setTitle(User);
		this.setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		panel = new JPanel(){ImageIcon i = new ImageIcon("C:\\Users\\jjc63\\Desktop\\StarCraft\\StarCraft\\img/map.jpg");
	    	public void paintComponent(Graphics g) {
	    		g.drawImage(i.getImage(), 0, 0, 640, 620, null);
	    	}
	    };
		panel.setLayout(null);

		
		makeComponent();
		getContentPane().add(panel, BorderLayout.CENTER);
		
		for(int i=0;i<2;i++){
			U_BU[i].addMouseListener(this);
		}
		U_selectBT.addActionListener(this);
		U_infoTB.getSelectionModel().addListSelectionListener(this);
		
		
		progressing = new Thread(this);
		progressing.start();
		
		Thread State = new Thread(new State());
		State.start();
		
	}
	public void setClan(Tribe clan){
		this.clan = clan;
	}
	public void setEnemy(StarUI enemy) //적 설정
	{
		this.enemy=enemy;
				
	}

	public void makeComponent()
	{
		CommandCenter.currentCommand = new JLabel();
		CommandCenter.currentCommand.setBounds(100, -130, 500, 500);
		CommandCenter.currentCommand.setOpaque(false);
		CommandCenter.currentCommand.setForeground(new Color(255,255,255));
		//CommandCenter.currentCommand.setText("노는중");
		panel.add(CommandCenter.currentCommand);
		
		
		jPanel1 = new JPanel();
		jPanel1.setLayout(null);
		jPanel1.setBounds(7, 13, 692, 84);
		jPanel1.setOpaque(false);
		panel.add(jPanel1);

		jPanel2 = new JPanel();
		jPanel2.setLayout(null);
		jPanel2.setBounds(15, 119, 450, 330);
		jPanel2.setOpaque(false);
		panel.add(jPanel2);
		
		int j = 30;
		for(int i = 0 ; i < 10; i++) {
		jPanel3[i] = new JPanel();
		jPanel3[i].setLayout(null);
		jPanel3[i].setBounds(j, 300, 150, 300);
		jPanel3[i].setOpaque(false);
		panel.add(jPanel3[i]);
		j += 50;
		}
		
		jPanel5 = new JPanel();
		jPanel5.setLayout(null);
		jPanel5.setBounds(5, 450, 630, 170);
		jPanel5.setOpaque(false);
		panel.add(jPanel5);
		
		
		
		U_mineralLB = new JLabel();
		U_mineralLB.setOpaque(false);
		U_mineralLB.setIcon(new ImageIcon("C:\\Users\\jjc63\\Desktop\\StarCraft\\StarCraft\\img/mineral.jpg"));
		U_mineralLB.setOpaque(false);
		U_mineralLB.setBounds(300, 5, 13, 15);
		jPanel1.add(U_mineralLB);

		U_gasLB = new JLabel();
		U_gasLB.setIcon(new ImageIcon("C:\\Users\\jjc63\\Desktop\\StarCraft\\StarCraft\\img/gas.jpg"));
		U_gasLB.setOpaque(false);
		U_gasLB.setBounds(400, 5, 13, 15);
		jPanel1.add(U_gasLB);

		U_unitLB = new JLabel();
		U_unitLB.setIcon(new ImageIcon("C:\\Users\\jjc63\\Desktop\\StarCraft\\StarCraft\\img/unit.jpg"));
		U_unitLB.setOpaque(false);
		U_unitLB.setBounds(500, 5, 13, 15);
		jPanel1.add(U_unitLB);
		
		U_BU[0]=new JLabel();
		U_BU[0].setBounds(25, 20, 150, 130);
		U_BU[0].setText(clan.getMainBName());
		U_BU[0].setIcon(clan.mainBIC);
		U_BU[0].setOpaque(false);
		jPanel2.add(U_BU[0]);

		int k =0;
		for(int i = 1; i < 11; i++) {
		U_BU[i] = new JLabel();
		U_BU[i].setIcon(clan.workerIC);
		U_BU[i].setText(clan.getWorkerName());
		U_BU[i].setOpaque(false);
		U_BU[i].setBounds(50, 40, 57, 57);
		jPanel3[k].add(U_BU[i]);
		k++;
		}
		
		CommandCenter.currentCommand = new JLabel();
		CommandCenter.currentCommand.setBounds(100, -130, 500, 500);
		CommandCenter.currentCommand.setOpaque(false);
		CommandCenter.currentCommand.setForeground(new Color(255,255,255));
		
		panel.add(CommandCenter.currentCommand);
		int z = 90;
		for(int i = 0 ; i < 10; i++) {
			wonjin[i] = new JLabel();
			wonjin[i].setBounds(z, 330, 150, 150);
			wonjin[i].setOpaque(false);
			wonjin[i].setForeground(new Color(255,255,255));
			
			panel.add(wonjin[i]);
			z += 50;
		}
		
		
		U_nameLB = new JLabel();
		U_nameLB.setText("이름");
		U_nameLB.setOpaque(false);
		U_nameLB.setForeground(new Color(255,255,255));
		U_nameLB.setBounds(10, 50, 45, 15);
		jPanel5.add(U_nameLB);

		U_powerLB = new JLabel();
		U_powerLB.setText("공격력");
		U_powerLB.setOpaque(false);
		U_powerLB.setForeground(new Color(255,255,255));
		U_powerLB.setBounds(10, 80, 45, 15);
		jPanel5.add(U_powerLB);

		U_countLB = new JLabel();
		U_countLB.setText("수량");
		U_countLB.setOpaque(false);
		U_countLB.setForeground(new Color(255,255,255));
		U_countLB.setBounds(10, 110, 45, 15);
		jPanel5.add(U_countLB);

		U_energyLB = new JLabel();
		U_energyLB.setText("에너지");
		U_energyLB.setOpaque(false);
		U_energyLB.setForeground(new Color(255,255,255));
		U_energyLB.setBounds(10, 140, 45, 15);
		jPanel5.add(U_energyLB);
		
		U_desc = new JLabel();
		U_desc.setHorizontalAlignment(SwingConstants.CENTER);
		U_desc.setOpaque(false);
		U_desc.setForeground(new Color(200,200,238));
		U_desc.setBounds(200,82,165,15);
		jPanel5.add(U_desc);
		
		U_progressingLB = new JLabel();
		U_progressingLB.setText("Progressing");
		U_progressingLB.setHorizontalAlignment(SwingConstants.CENTER);
		U_progressingLB.setOpaque(false);
		U_progressingLB.setForeground(new Color(200,200,238));
		U_progressingLB.setBounds(235,112,100,15);
		jPanel5.add(U_progressingLB);
		
		U_tribe = new JLabel();
		U_tribe.setText(clan.getTribeName());
		U_tribe.setOpaque(false);
		U_tribe.setForeground(new Color(32,253,44));
		U_tribe.setBounds(20, 5, 50, 15);
		jPanel1.add(U_tribe);

		U_mineral = new JLabel();
		U_mineral.setText(String.valueOf(clan.getMineral()));
		U_mineral.setForeground(new Color(32,253,44));
		U_mineral.setOpaque(false);
		U_mineral.setBounds(320, 5, 50, 15);
		jPanel1.add(U_mineral);

		U_gas = new JLabel();
		U_gas.setText(String.valueOf(clan.getGas()));
		U_gas.setForeground(new Color(32,253,44));
		U_gas.setOpaque(false);
		U_gas.setBounds(420, 5, 50, 15);
		jPanel1.add(U_gas);

		U_unit = new JLabel();
		U_unit.setText(String.valueOf(clan.getUnit()));
		U_unit.setOpaque(false);
		U_unit.setForeground(new Color(32,253,44));
		U_unit.setBounds(520, 5, 22, 15);
		jPanel1.add(U_unit);
		
		U_slush = new JLabel();
		U_slush.setText("/");
		U_slush.setOpaque(false);
		U_slush.setForeground(new Color(32,253,44));
		U_slush.setBounds(540, 5, 5, 15);
		jPanel1.add(U_slush);
		
		U_maxunit = new JLabel();
		U_maxunit.setText(String.valueOf(clan.getMaxunit()));
		U_maxunit.setOpaque(false);
		U_maxunit.setForeground(new Color(32,253,44));
		U_maxunit.setBounds(550, 5, 25, 15);
		jPanel1.add(U_maxunit);
		
		U_name = new JLabel();
		U_name.setForeground(new Color(32,253,44));
		U_name.setBounds(80, 50, 70, 15);
		jPanel5.add(U_name);

		U_power = new JLabel();
		U_power.setOpaque(false);
		U_power.setForeground(new Color(32,253,44));
		U_power.setBounds(80, 80, 70, 15);
		jPanel5.add(U_power);

		U_cnt = new JLabel();
		U_cnt.setOpaque(false);
		U_cnt.setForeground(new Color(32,253,44));
		U_cnt.setBounds(80, 110, 70, 15);
		jPanel5.add(U_cnt);
		
		U_face = new JLabel();
		U_face.setOpaque(false);
		U_face.setBounds(388,103,60,47);
		jPanel5.add(U_face);
		
		U_message = new JLabel();
		U_message.setForeground(Color.WHITE);
		U_message.setFont(new Font("Dialog.plain", 0, 15));
		U_message.setHorizontalAlignment(SwingConstants.CENTER);
		U_message.setOpaque(false);
		U_message.setBounds(195,0,250,30);
		jPanel5.add(U_message);

		U_energyPB = new JProgressBar();
		U_energyPB.setStringPainted(true);
		U_energyPB.setMinimum(0);
		U_energyPB.setMaximum(100);
		U_energyPB.setValue(0);
		U_energyPB.setOpaque(false);
		U_energyPB.setForeground(Color.RED);
		U_energyPB.setBounds(70, 140, 90, 20);
		jPanel5.add(U_energyPB);

		
		U_progressPB = new JProgressBar();
		U_progressPB.setOpaque(false);
		U_progressPB.setStringPainted(true);
		U_progressPB.setMinimum(0);
		U_progressPB.setMaximum(100);
		U_progressPB.setValue(0);
		U_progressPB.setForeground(Color.BLUE);
		U_progressPB.setBounds(220, 130, 124, 27);
		jPanel5.add(U_progressPB);

		U_infoTB_model_vector = new Vector();
		U_infoTB_model_vector.addElement("이름");
		U_infoTB_model_vector.addElement("상태");
		U_infoTB_DTM = new DefaultTableModel(U_infoTB_model_vector, 0){ //셀편집안되게 처리
			   public boolean isCellEditable(int row,int col){
				    return false;
				   }
				  };
		U_infoTB = new JTable(U_infoTB_DTM);
		U_infoTB.setOpaque(true);
		U_infoTB_scroll = new JScrollPane(U_infoTB);
		U_infoTB_scroll.setBounds(480, 10, 150, 90);
		jPanel5.add(U_infoTB_scroll);
		
		cb = new JComboBox();
		cb.setOpaque(false);
		cb.setBounds(500, 105, 110, 20);
		jPanel5.add(cb);
		
		U_selectBT = new JButton();
		U_selectBT.setText("선택");
		U_selectBT.setOpaque(false);
		U_selectBT.setForeground(new Color(-13421773));
		U_selectBT.setBounds(515, 130, 80, 30);
		jPanel5.add(U_selectBT);
	}
	
	synchronized public void setTB(ArrayList workerVT){ // 다중 테이블 선택시 여러개의 스레드가(예 일꾼스레드)동시에 접근하여 테이블수정하는것을 막음.
		DefOB = new Object[2];
		StarObject so;
		
		while(U_infoTB_DTM.getRowCount()>=1){
			U_infoTB_DTM.removeRow(0);
		}
		for(int index=0;index<workerVT.size();index++){
			so=(StarObject)(workerVT.get(index));
			DefOB[0]=so.getName();
			DefOB[1]=so.getCondition();
			
			U_infoTB_DTM.insertRow(index, DefOB);
		}
	}
	public void setMessage(String msg){
		for(int i = 0; i<4;i++){
			U_message.setText(msg);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {}
			U_message.setText("");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {}
		}
	}	
	public void mouseClicked(MouseEvent me){
		if(me.getSource()==U_BU[0]){
			job=0;
			U_desc.setText(clan.mainBdesc);
			U_face.setIcon(clan.buildingface);
			setInfo(clan.getMainBName(), "없음", String.valueOf(clan.mainBVT.size()));
			setTB(clan.mainBVT);
			U_energyPB.setValue(0);
			U_energyPB.setMaximum(clan.getMainBEnergy());
			U_progressPB.setValue(0);
			cb.removeAllItems();
			for(int i=0;i<clan.mainBItem.size();i++)
				cb.addItem(clan.mainBItem.get(i));
		}
		if(me.getSource()==U_BU[1]){
			job=0;
			U_desc.setText(clan.workerdesc);
			U_face.setIcon(clan.workerface);
			setInfo(clan.getWorkerName(), String.valueOf(clan.getWorkerpower())+" + "+String.valueOf(clan.getUpgrade()), String.valueOf(clan.workerVT.size()));
			setTB(clan.workerVT);
			U_energyPB.setValue(0);
			U_energyPB.setMaximum(clan.getWorkerEnergy());
			U_progressPB.setValue(0);
			cb.removeAllItems();
			for(int i=0;i<clan.workerItem.size();i++)
				cb.addItem(clan.workerItem.get(i));
		}
		
	}
		
	public void mouseEntered(MouseEvent me){
		for(int i=0;i<2;i++){				
			if(me.getSource()==U_BU[i]){
				U_BU[i].setBorder(new BevelBorder(BevelBorder.RAISED));
			}
		}
	}
	public void mouseExited(MouseEvent me){
		for(int i=0;i<2;i++){
			if(me.getSource()==U_BU[i]){
				U_BU[i].setBorder(null);
			}
		}
	}
	public void mousePressed(MouseEvent me){
		for(int i=0;i<2;i++){			
			if(me.getSource()==U_BU[i]){
				U_BU[i].setBorder(new BevelBorder(BevelBorder.LOWERED));
			}
		}
	}
	public void mouseReleased(MouseEvent me){
		for(int i=0;i<2;i++){			
			if(me.getSource()==U_BU[i]){
				U_BU[i].setBorder(new BevelBorder(BevelBorder.RAISED));
			}
		}
	}
	
	public void ChangeJob(int job, ArrayList workerVT){
		int index[];
		index=U_infoTB.getSelectedRows(); //다중 선택 행 번호 반환
		for(int i=0;i<index.length;i++){
			((StarObject)(workerVT.get(index[i]))).go.interrupt(); //하던일 중지 예외 발생시킴 스레드 죽임
			((StarObject)(workerVT.get(index[i]))).setFlag(false); //하던일 중지 시킨다음
			((StarObject)(workerVT.get(index[i]))).setJob(job); //새로운 작업 부여
			((StarObject)(workerVT.get(index[i]))).getThread().resume();  //시작
		}
	}
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e){
		int[] i;
		i=U_infoTB.getSelectedRows();
		if(e.getSource()==U_selectBT){ //수행 버튼이 클릭되면
			if(U_name.getText()==clan.getWorkerName()){
				if((String)cb.getSelectedItem()=="미네랄"){
					ChangeJob(1,clan.workerVT);					
				}
				else if((String)cb.getSelectedItem()=="가스"){
					ChangeJob(2,clan.workerVT);
				}
				else if((String)cb.getSelectedItem()==clan.getMainBName()){					
					ChangeJob(3,clan.workerVT);
				}
				else if((String)cb.getSelectedItem()=="공격하기"){
					ChangeJob(4,clan.workerVT);
				}
			}
			
			if(U_name.getText()==clan.getMainBName()){
				if((String)cb.getSelectedItem()==clan.getWorkerName()){
					ChangeJob(1,clan.mainBVT);
				}
			}
		
			
		}
	}
	public void setInfo(String name, String power, String count){
		U_name.setText(name);
		U_power.setText(power);
		U_cnt.setText(count);
	}
	public void run(){
		while(true){
			switch(job){
			case 0:
				U_progressPB.setValue(0);
				try { //시스템 과부하 줄이기 위해 텀줌..없애면 다운 먹음..리소스 엄청 먹는다...
					Thread.sleep(10);
				} catch (InterruptedException e) {}
				break;
			case 1:
				setProgress(clan.mainBVT);
				if(clan.mainBVT.size()!=0)
					setEnergy(clan.mainBVT);
				try { //시스템 과부하 줄이기 위해 텀줌..없애면 다운 먹음..리소스 엄청 먹는다...
						Thread.sleep(10);
					} catch (InterruptedException e) {}
				break;
			case 2:
				setProgress(clan.workerVT);
				if(clan.workerVT.size()!=0)
					setEnergy(clan.workerVT);
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {}
				break;			
			
			}
		}
	}
	public void setEnergy(ArrayList workerVT){
		StarObject so;
		if(workerVT.size()!=0){
			try{
			so=(StarObject)(workerVT.get(rowcnt));
			U_energyPB.setValue(so.energy);
			}catch(Exception e){}
			
		}
	}
	public void setProgress(ArrayList mainBVT){
		StarObject so;
		if(mainBVT.size()!=0){
			try{
				so=(StarObject)(mainBVT.get(getRowcnt()));
				U_progressPB.setValue(so.progress);
			}
			catch(Exception e){}
		}
	}
	synchronized int getRowcnt(){
		return rowcnt;
	}
	public void valueChanged(ListSelectionEvent event) {
		if(!U_infoTB.getSelectionModel().isSelectionEmpty()){ //테이블 선택되었을 경우
			rowcnt=U_infoTB.getSelectedRow(); //테이블 선택 행 번호 얻기
			if(U_infoTB_DTM.getValueAt(0, 0)==clan.getMainBName()){ //본건물 선택되었을 경우
				job=1; //프로그레스바 스레드 돌리기
			}
			if(U_infoTB_DTM.getValueAt(0, 0)==clan.getWorkerName()){//일꾼 선택되었을경우
				job=2;	
			}
			
		}
		else{
			U_progressPB.setValue(0);
			U_energyPB.setValue(0);
		}
    }
	public void endMessage(String msg){ //종료 메세지
		JOptionPane.showMessageDialog(this,msg);
	}
}

class State implements Runnable {
	public void run() {
		while (true) {
			for (int i = 0 ; i < Terran.workerVT.size(); i++) {
				try { 
					Thread.sleep(100);
				} catch (InterruptedException e) {
					
				}
				if (((StarObject) Terran.workerVT.get(i)).getCondition().equals("미네랄")) {
					StarUI.wonjin[i].setText("m : " + ((StarObject) Terran.workerVT.get(i)).getResources());
					
				} else if (((StarObject) Terran.workerVT.get(i)).getCondition().equals("가스")) {
					StarUI.wonjin[i].setText("g : " + ((StarObject) Terran.workerVT.get(i)).getResources());
				} else if (((StarObject) Terran.workerVT.get(i)).getCondition().equals("커멘드")) {
					StarUI.wonjin[i].setText("커멘드");
				} else {
					StarUI.wonjin[i].setText("노는중");
				}
			}
		}
	}
}