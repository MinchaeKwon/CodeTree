/**
 * 루돌프의 반란
 * 삼성 SW 역량테스트 2023 하반기 오후 1번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/rudolph-rebellion/description?page=1&pageSize=20
 * 
 * @author minchae
 * @date 2023. 10. 27.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

class Santa {
	int n;
	int x;
	int y;
	
	public Santa(int n, int x, int y) {
		this.n = x;
		this.x = x;
		this.y = y;
	}
}

public class Main {
	
	// 상하좌우, 대각선
	static int[] dx = {-1, 1, 0, 0, -1, -1, 1, 1};
	static int[] dy = {0, 0, -1, 1, -1, 1, -1, 1};
	
	static int N, M, P, C, D;
	static int rx, ry; // 루돌프의 위치
	
	static int[][] map; // 산타와 루돌프의 위치 저장 (산타 번호, 루돌프 -1)
	static Santa[] santaArr;
	
	static int[] stun; // 산타가 기절했는지 확인
	static boolean[] dead; // 산타가 죽었는지 확인
	
	static int[] score; // 산타가 얻은 점수 저장

	public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        N = Integer.parseInt(st.nextToken()); // 게임판의 크기
        M = Integer.parseInt(st.nextToken()); // 게임 턴 수
        P = Integer.parseInt(st.nextToken()); // 산타의 수
        C = Integer.parseInt(st.nextToken()); // 루돌프의 힘
        D = Integer.parseInt(st.nextToken()); // 산타의 힘
        
        map = new int[N][N];
        santaArr = new Santa[P + 1];
        stun = new int[P + 1];
        dead = new boolean[P + 1];
        score = new int[P + 1];
        
        st = new StringTokenizer(br.readLine());
        
        rx = Integer.parseInt(st.nextToken()) - 1;
        ry = Integer.parseInt(st.nextToken()) - 1;
        
        map[rx][ry] = -1;
        
        for (int i = 0; i < P; i++) {
        	st = new StringTokenizer(br.readLine());
        	
        	int num = Integer.parseInt(st.nextToken());
        	int x = Integer.parseInt(st.nextToken()) - 1;
        	int y = Integer.parseInt(st.nextToken()) - 1;
        	
        	santaArr[num] = new Santa(num, x, y);
        	
        	map[x][y] = num;
        }
        
        /*
         * 
         * */
        
        // 게임 턴 수만큼 반복
        while (M-- > 0) {
        	// 1. 루돌프가 가장 가까운 산타를 향해 돌진
        	moveRudolph();
        	
        	// 2. 산타가 움직임
        	moveSanta();
        	
        	getScore();
        }
        
        // 산타가 얻은 최종점수 출력
        for (int i = 1; i <= P; i++) {
        	System.out.print(score[i] + " ");
        }
        
	}
	
	private static void moveRudolph() {
		
	}
	
	private static void moveSanta() {
		for (int i = 1; i <= P; i++) {
			// 격자 밖을 벗어난 산타일 경우 다음으로 넘어감
			if (dead[i]) {
				continue;
			}
			
			
		}
	}
	
	// 산타 점수 증가
	private static void getScore() {
		for (int i = 1; i <= P; i++) {
			if (!dead[i]) {
				score[i]++;
			}
		}
	}
	
	private static boolean isRange(int x, int y) {
		return x >= 0 && x < N && y >= 0 && y < N;
	}

}
