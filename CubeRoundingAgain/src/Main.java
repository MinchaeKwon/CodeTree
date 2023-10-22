/**
 * 정육면체 한번 더 굴리기
 * 삼성 SW 역량테스트 2021 하반기 오전 1번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/cube-rounding-again/description?page=2&pageSize=20
 * 
 * @author minchae
 * @date 2023. 10. 22.
 * 
 * 백준 23288 주사위 굴리기 2
 * https://www.acmicpc.net/problem/23288
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
	
	// 우하좌상
	static int[] dx = {0, 1, 0, -1};
	static int[] dy = {1, 0, -1, 0};
	
	static int n, m;
	static int[][] map;
	
	static int[] dice = {1, 2, 3, 4, 5, 6}; // 윗면 1, 앞면 2, 오른쪽면 3, 왼쪽면 4, 뒷면 5, 아랫면 6
	
	static int cx, cy; // 주사위 위치
	static int dir = 0; // 주사위 방향 (초기 방향은 오른쪽)
	
	// 주사위가 놓여있는 상태 
    public static int u = 1, f = 2, r = 3;
	
	public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        
        map = new int[n][n];
        
        for (int i = 0; i < n; i++) {
        	st = new StringTokenizer(br.readLine());
        	
        	for (int j = 0; j < n; j++) {
        		map[i][j] = Integer.parseInt(st.nextToken());
        	}
        }
        
        int result = 0;
        
        while (m-- > 0) {
        	checkMove();
            moveDice();
            
            result += getScore(map[cx][cy]);
            
            changeNextDir();
        	
        }
        
        System.out.println(result);
	}
	
	// 주사위가 해당 방향으로 이동 가능한지 확인
	private static void checkMove() {
		int nx = cx + dx[dir];
		int ny = cy + dy[dir];
		
		// 범위 벗어나는 경우 방향 반대로 바꿈
		if (!isRange(nx, ny)) {
			dir = (dir + 2) % 4; 
		}
		
		cx += dx[dir];
		cy += dy[dir];
	}
	
	// 주사위 굴리기
	private static void moveDice() {
		int[] tmp = Arrays.copyOf(dice, 6);

        // 정육면체 굴리기 - 윗면은 1, 앞면은 2, 오른쪽면은 3, 왼쪽면은 4, 뒷면은 5, 아랫면은 6
        switch (dir) {
            case 0: // 동쪽
                dice[0] = tmp[3];
                dice[2] = tmp[0];
                dice[3] = tmp[5];
                dice[5] = tmp[2];
                break;
        
            case 1: // 남쪽
            	dice[0] = tmp[4];
                dice[1] = tmp[0];
                dice[4] = tmp[5];
                dice[5] = tmp[1];
                break;

            case 2: // 서쪽
            	dice[0] = tmp[2];
                dice[2] = tmp[5];
                dice[3] = tmp[0];
                dice[5] = tmp[3];
                break;

            case 3: // 북쪽
            	dice[0] = tmp[1];
                dice[1] = tmp[5];
                dice[4] = tmp[0];
                dice[5] = tmp[4];
                break;
        }
	}
	
	// 격자판 위 주사위가 놓여있는 칸에 적혀있는 숫자와 상하좌우로 인접하며 같은 숫자가 적혀있는 모든 칸의 합만큼 점수를 얻음
	private static int getScore(int num) {
		Queue<int[]> q = new LinkedList<>();
		boolean[][] visited = new boolean[n][n];
		
		q.add(new int[] {cx, cy});
		visited[cx][cy] = true;
		
		int cnt = 1;
		
		while (!q.isEmpty()) {
			int[] cur = q.poll();
			
			for (int i = 0; i < 4; i++) {
				int nx = cur[0] + dx[i];
				int ny = cur[1] + dy[i];
				
				if (isRange(nx, ny) && !visited[nx][ny] && map[nx][ny] == num) {
					q.add(new int[] {nx, ny});
					visited[nx][ny] = true;
					
					cnt++;
				}
			}
		}
		
		return map[cx][cy] * cnt;
	}
	
	// 주사위 이동 방향 결정
	private static void changeNextDir() {
		if (dice[5] > map[cx][cy]) {
			// 주사위의 아랫면이 보드의 해당 칸에 있는 숫자보다 크면 현재 진행방향에서 90' 시계방향으로 회전
			dir = (dir + 1) % 4;
		} else if (dice[5] < map[cx][cy]) {
			// 주사위의 아랫면의 숫자가 더 작다면 현재 진행방향에서 90' 반시계방향으로 회전
			dir = (dir + 3) % 4;
		}
	}
	
	private static boolean isRange(int x, int y) {
		return x >= 0 && x < n && y >= 0 && y < n;
	}
	
}
