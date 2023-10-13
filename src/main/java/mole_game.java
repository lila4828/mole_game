import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.List;

public class mole_game {
    private Image backgroundImg, main_backgroundImg; // 게임 배경 이미지, 메인 화면 배경 이미지
    private Image hammerImg, hitImg; // 해머 이미지
    private Image moleImg; // 두더지 이미지
    private HashSet<Integer> pressedKeys; // 눌린 키를 다루기 위한 해시셋
    private int molegame_score; // 점수
    private JLabel score_label; // 점수 표시할 칸
    private List<Mole> moles; // 두더지 클래스 리스트
    private Hammer hammer; // 플레이어
    private JPanel startPanel, gamePanel; // 시작 화면 패널, 게임 패널
    private JButton easyButton, hardButton, exitButton; // 난이도 선택 버튼과 종료 버튼
    private boolean gameStarted = false; // 게임 시작 여부 부울값
    private boolean gameOver = false; // 게임 종료 여부 부울값
    private boolean missCheck1 = true; // 플레이어별 미스 여부 체크 부울값

    public static class MyFrame extends JFrame {
        startPanel = new JPanel() { // 초기 화면용 JPanel 생성
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (main_backgroundImg != null) { // 배경 적용
                    g.drawImage(main_backgroundImg, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        startPanel.setLayout(null); // 상하좌우 배치하는 Layout 설정
        startPanel.setSize(getWidth(), getHeight());
        Font buttonFont = new Font("SansSerif", Font.PLAIN, 16); // 버튼 전용 폰트

        JLabel titleLabel = new JLabel("두더지 게임"); // 타이틀 JLabel 생성
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 30)); // 타이틀 폰트 설정
        titleLabel.setBounds(320, 30, 200, 50);
        //titleLabel.setHorizontalAlignment(JLabel.CENTER); // 글 중앙 정렬
        startPanel.add(titleLabel); // startPanel의 북쪽에 타이틀 Label 위치시킴

        easyButton = new JButton("초보자용");
        hardButton = new JButton("상급자용");
        exitButton = new JButton("종료"); // 버튼 세개에 JButton 할당

        easyButton.setBounds(300, 100, 200, 50);
        hardButton.setBounds(300, 180, 200, 50);
        exitButton.setBounds(300, 260, 200, 50); // 버튼들 위치와 크기 설정
        easyButton.setFont(buttonFont);
        hardButton.setFont(buttonFont);
        exitButton.setFont(buttonFont); // 버튼들 폰트 설정

        startPanel.add(easyButton);
        startPanel.add(hardButton);
        startPanel.add(exitButton); // 버튼들 패널에 추가

        easyButton.addActionListener(new ActionListener() { // 초보자용 버튼 작동
            @Override
            public void actionPerformed(ActionEvent e) {
                diffcultSet(3000); // 딜레이 설정
                startPanel.setVisible(false); // 시작화면 안보이게
                gameStarted = true; // paint용 변수 설정
                gameStart(); // 게임 시작
                requestFocus(); // 키 입력 받을 수 있도록 포커스 요청
            }
        });
        hardButton.addActionListener(new ActionListener() { // 상급자용 버튼 작동
            @Override
            public void actionPerformed(ActionEvent e) {
                diffcultSet(1500); // 딜레이 설정
                startPanel.setVisible(false); // 시작화면 안보이게
                gameStarted = true; // paint용 변수 설정
                gameStart(); // 게임 시작
                requestFocus(); // 키 입력 받을 수 있도록 포커스 요청
            }
        });
        exitButton.addActionListener(new ActionListener() { // 종료 버튼 작동
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // 종료
            }
        });

        add(startPanel); // 시작 화면을 프레임에 추가
        startPanel.setVisible(true); // 시작 화면을 표시
    }
    public void paint(Graphics g){ // 배경은 유지, 해머와 두더지만 계속 갱신
        if(gameStarted){ // 게임 시작 여부에 따라 startPanel을 그리거나 게임 화면을 그리거나
            if (backgroundImg != null) {
                g.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), this); // 배경 이미지 설정
            }
            if (moleImg != null){
                for (Mole mole: moles){
                    g.drawImage(moleImg, mole.getX(), mole.getY(), this); // 두더지들 그려냄
                }
            }
            if (hammerImg != null){
                hammer.draw(g); //  해머 그리기
            }
            if (score_label != null) {
                score_label.repaint(); // 점수판 repaint
            }
        }
        else {
            startPanel.repaint(); // 시작 패널 그림
        }
    }
    private void gameStart(){ // 게임 시작 관리 함수
        for (Mole mole : moles){ // 각 두더지별로 적용
            mole.timerstart(); // 타이머 ON
        }

        score_label = new JLabel(); // 1번 플레이어 점수 기록용 label 생성
        score_label.setText("" + molegame_score); // 텍스트 설정

        Font score_font = new Font("궁서", Font.BOLD, 20); // 폰트 설정
        score_label.setFont(score_font); // 폰트 적용

        score_label.setBounds(20, 20, 30, 30); // 점수판 띄움
        this.add(score_label); // 점수판 JLabel JFrame에 추가
    }

    private void checkGameOver() { // 게임 종료 관리 함수
        if (molegame_score >= 15) { // 15점 넘으면 승리
            gameOver = true;
            showGameOverMessage("Player", molegame_score, molegame_score); // 플레이어 승리 메시지 표시
        }
    }

    private void showGameOverMessage(String winner, String loser, int winnerScore,
                                     int loserScore, int winnerMiss, int loserMiss) {
        writeResultsFile(winner, winnerScore, winnerMiss, true);
        writeResultsFile(loser, loserScore, loserMiss, false); // 승자, 패자 모두 기록


        JOptionPane.showMessageDialog(this, "YOU WIN! Player " + winner, "Game Over", JOptionPane.INFORMATION_MESSAGE);
        // 게임 종료 메시지를 보여주고 종료함
        System.exit(0); // 게임 종료
    }

    public void keyPressed(KeyEvent e) { // 키 누름처리, 해시셋에 키 코드가 저장됨
        int keyCode = e.getKeyCode(); // 키코드 받아오기
        pressedKeys.add(keyCode); // 해시셋에 누른 키 포함시킴

        hammer.move(pressedKeys); // hammer 움직이기

        if (keyCode == KeyEvent.VK_SPACE){ // 스페이스를 눌렀을 경우
            hammer.hit();
            missCheck = true; // 미스를 확인해야 하는지 여부를 true로
            for(Mole mole : moles){
                if((hammer.getX() <= mole.getX() + 50) && (hammer.getX() >= mole.getX() - 50) // 1p의 망치가 두더지 이미지의 좌표범위와 일치하는 경우
                        && (hammer.getY() <= mole.getY() + 50) && (hammer.getY() <= mole.getY() + 50)){
                    molegame_score ++; // 점수 획득
                    System.out.println("Score : " + molegame_score); // 임시 점수출력
                    score_label.setText("" + molegame_score); // 1P 점수 설정
                    resetMole(mole); // 두더지 초기화
                    checkGameOver(); // 15점 달성시 종료 함수 호출
                } else {
                    if (missCheck){
                        molegame_miss ++; // 빗나감 횟수 증가
                        missCheck = false; // 미스를 확인해야 하는지 여부를 false로
                    }
                }
            }
        }
        repaint(); // repaint
    }

    @Override
    public void keyReleased(KeyEvent e){ // 키 땜처리, 해시셋에 키 코드가 삭제됨
        int keyCode = e.getKeyCode(); // 땐 키코드 받아옴
        pressedKeys.remove(keyCode); // 해시셋에서 해당 키코드 제거
        repaint(); // 다시 그림
    }

    @Override
    public void keyTyped(KeyEvent e){ }

    private void resetMole(Mole mole) { // 두더지를 망치로 때렸을 때 위치 변경과 타이머 초기화
        mole.setX((int)(Math.random() * (getWidth() - 50)));
        mole.setY((int)(Math.random() * (getHeight() - 50)));
        mole.reset();
    }

    private void diffcultSet(int delay){ // 두더지가 사라졌다 나타나는 간격 조절용 함수
        for (Mole mole : moles) {
            mole.delaySet(delay); // 각 두더지마다 주기 설정
        }
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MyFrame();
            }
        });
    }
}

class Mole extends JFrame{ // 한마리의 두더지가 아닌 여러 마리의 두더지를 다루기 위한 Mole 클래스
    private int x, y; // 두더지 좌표
    private Timer moleTimer; // 두더지에게 적용되는 타이머
    private int delay; // 타이머 주기

    public Mole() { // Mole 클래스 생성자, 좌표값과 delay 설정 후 Timer 설정
        x = -100; y = - 100; delay = 3000;
        moleTimer = new Timer(delay, (ActionEvent e) -> { // 타이머 동작, 딜레이마다 위치 재조정
            x = (int)(Math.random() * (800 - 50)); // 랜덤 x좌표
            y = (int)(Math.random() * (400 - 50)); // 랜덤 y좌표
            repaint(); // 다시 그림
        }); // 두더지가 타이머에 따라 랜덤한 위치로 이동함
    }
    public int getX() { return x; } // private된 두더지의 x좌표 획득
    public int getY() { return y; } // private된 두더지의 y좌표 획득
    public void setX(int x) { this.x = x; } // x좌표 설정
    public void setY(int y) { this.y = y; } // y좌표 설정
    public void reset() { // 두더지를 초기화시키는 함수 reset()
        x = -100; y = -100; // 화면 밖으로 옮김
        moleTimer.restart(); // 타이머 재시작함
        moleTimer.setInitialDelay(delay); // 딜레이 재설정
    }
    public void delaySet(int delay) {this.delay = delay;} // 난이도 선택 시 두더지 이동 시간 설정
    public void timerstart() { moleTimer.start(); } // 타이머 킴
}
class Hammer { // Hammer 클래스
    private int hammer;
    private int up,down,left,right;
    private Image hammerImg, hammerImgbackup, hitImg;
    private Timer hitTimer;

    Hammer(int hammer, Image hammerImg, Image hitImg,
           int up, int down, int left, int right){
        this.hammer = hammer;// 좌표 설정
        this.hammerImg = hammerImg; this.hitImg = hitImg; // 이미지 설정
        this.up = up;   this.down = down; // 상하 키 설정
        this.left = left;   this.right = right; // 좌우 키 설정

        hitTimer = new Timer(200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { // 타이머가 켜진 후 0.5초 뒤에 꺼짐
                release();
                hitTimer.stop();
            }
        });
        hitTimer.setRepeats(false); // 한번만 동작하게끔 타이머 설정
    }

    public void move(HashSet<Integer> pressedKeys) {
        if (pressedKeys.contains(down)){ // 하단 키 누르면 하단으로 20만큼 이동
            if((hammerY + 20) < 350){
                hammerY += 20;
            }
        }
        if (pressedKeys.contains(up)){ // 상단 키 누르면 상단으로 20만큼 이동
            if((hammerY - 20) >= -50){
                hammerY -= 20;
            }
        }
        if (pressedKeys.contains(right)){ // 우측 키 누르면 우측으로 20만큼 이동
            if((hammerX + 20) < 750){
                hammerX += 20;
            }
        }
        if (pressedKeys.contains(left)){ // 좌측 키 누르면 좌측으로 20만큼 이동
            if((hammerX - 20) >= -50){
                hammerX -= 20;
            }
        }
    }

    public int getX(){ return hammerX; } // private된 망치 X좌표 얻기
    public int getY(){ return hammerY; } // private된 망치 Y좌표 얻기
    public void hit() {
        if(!hitTimer.isRunning()) {
            hammerImgbackup = hammerImg;
            hammerImg = hitImg;
            hitTimer.start();
            playSound("squeaky-toy.wav");
        }
    }

    public void release() {
        hammerImg = hammerImgbackup; // 이미지 원상 복구
    }

    public void draw(Graphics g){
        g.drawImage(hammerImg, hammer, null);
    }

}