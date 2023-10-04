/**
 * 술래잡기 체스
 * 삼성 SW 역량테스트 2020 상반기 오전 2번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/odd-chess2/description?page=2&pageSize=20
 * 
 * @author minchae
 * @date 2023. 9. 19.
 * 
 * 백준 19236 청소년 상어
 * https://www.acmicpc.net/problem/19236
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

class Tagger {
	int x;
	int y;
	int sum; // 술래말이 잡은 도둑말 번호의 누적 합
	int d; // 술래말이 잡은 도둑말 방향
	
	public Tagger(int x, int y, int sum, int d) {
		this.x = x;
		this.y = y;
		this.sum = sum;
		this.d = d;
	}
}

class Thief {
	int x;
	int y;
	int n;
	int d; // 방향 d는 0부터 순서대로 ↑, ↖, ←, ↙, ↓, ↘, →, ↗
	boolean isCatch; // 잡혔는지 확인
	
	public Thief(int x, int y, int n, int d, boolean isCatch) {
		this.x = x;
		this.y = y;
		this.n = n;
		this.d = d;
		this.isCatch = isCatch;
	}
}

public class Main {
	
	static int[] dx = {-1, -1, 0, 1, 1, 1, 0, -1};
	static int[] dy = {0, -1, -1, -1, 0, 1, 1, 1};
	
	static int result = 0;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		int[][] map = new int[4][4]; // 술래/도둑말, 빈칸 현황 (술래말 -1, 빈 칸 0)
		Thief[] thief = new Thief[17];
		
		for (int i = 0; i < 4; i++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			
			for (int j = 0; j < 4; j++) {
				int p = Integer.parseInt(st.nextToken());
				int d = Integer.parseInt(st.nextToken()) - 1;
				
				map[i][j] = p;
				thief[p] = new Thief(i, j, p, d, false);
			}
		}

		// 초기에는 (0, 0)에 있는 도둑말을 잡으며 시작
		Thief cur = thief[map[0][0]];
		
		cur.isCatch = true;
		map[0][0] = -1;
		
		Tagger tagger = new Tagger(0, 0, cur.n, cur.d);
		backtracking(tagger, map, thief);
		
		System.out.println(result);
	}
	
	// 백트래킹 이용해 잡을 수 있는 도둑말의 번호 합의 최댓값 구함
	private static void backtracking(Tagger tagger, int[][] map, Thief[] thief) {
		result = Math.max(result, tagger.sum); // 잡은 도둑말 번호 합 갱신
		
		moveThief(map, thief);
		
		moveTagger(tagger, map, thief);
	}
	
	// 도둑말 이동시키기
	private static void moveThief(int[][] map, Thief[] thief) {
		for (Thief t : thief) {
			// 이미 잡힌 경우에는 다음 도둑말로 넘어감
			if (t == null || t.isCatch) {
				continue;
			}
			
			// 8방향 탐색
			for (int i = 0; i < 8; i++) {
				int nd = (t.d + i) % 8;
				int nx = t.x + dx[nd];
				int ny = t.y + dy[nd];
				
				if (nx >= 0 && nx < 4 && ny >= 0 && ny < 4 && map[nx][ny] != -1) {
					if (map[nx][ny] == 0) { // 빈 칸인 경우
						map[t.x][t.y] = 0;
					} else { // 다른 도둑말이 있는 경우
						Thief change = thief[map[nx][ny]];
						
						change.x = t.x;
						change.y = t.y;
						
						map[t.x][t.y] = change.n; // 순서만 바꿈
					}
					
					map[nx][ny] = t.n; // 도둑말 이동
					
					// 도둑말 위치 갱신
					t.x = nx;
					t.y = ny;
					t.d = nd;
					
					break; // 도둑말이 이동한 경우에는 for문 탈출
				}
				
			}
		}
	}
	
	// 술래말 이동시키기
	private static void moveTagger(Tagger tagger, int[][] map, Thief[] thief) {
		// 체스판이 4 x 4 크기이므로 최대 3칸까지 이동 가능
		for (int cnt = 1; cnt < 4; cnt++) {
			int nx = tagger.x + dx[tagger.d] * cnt;
			int ny = tagger.y + dy[tagger.d] * cnt;
			
			// 범위를 벗어나지 않고, 도둑말이 있는 경우
			if (nx >= 0 && nx < 4 && ny >= 0 && ny < 4 && map[nx][ny] > 0) {
				// 원본 배열 복사해서 사용 -> 백트래킹 사용하기 때문 (배열을 복사해서 사용하기 때문에 원본 배열을 복구시킬 필요없음)
				
				int[][] copyMap = new int[4][4];
				
				for (int i = 0; i < 4; i++) {
					copyMap[i] = Arrays.copyOf(map[i], 4);
				}
		
                Thief[] copyThief = new Thief[17];
                for (int i = 1; i < 17; i++) {
                	copyThief[i] = new Thief(thief[i].x, thief[i].y, thief[i].n, thief[i].d, thief[i].isCatch);
                }
				
				copyMap[tagger.x][tagger.y] = 0; // 원래 술래말이 있던 위치는 빈 칸으로 만듦
				copyMap[nx][ny] = -1; // 술래말 위치 갱신
				
				Thief catchThief = copyThief[map[nx][ny]];
				
				catchThief.isCatch = true;
				
				Tagger newTagger = new Tagger(nx, ny, catchThief.n + tagger.sum, catchThief.d);
				backtracking(newTagger, copyMap, copyThief);
			}
		}
	}

}
