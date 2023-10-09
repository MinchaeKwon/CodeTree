/**
 * 놀이기구 탑승
 * 삼성 SW 역량테스트 2021 상반기 오전 1번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/go-on-the-rides/description?page=2&pageSize=20
 * 
 * @author minchae
 * @date 2023. 10. 10.
 * 
 * 백준 21608 상어 초등학교
 * https://www.acmicpc.net/problem/21608
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

class Student implements Comparable<Student> {
	int x;
	int y;
	int like; // 인접한 칸 중 앉아있는 좋아하는 친구의 수
	int empty; // 인접한 칸 중 비어있는 칸의 수
	
	public Student(int x, int y, int like, int empty) {
		this.x = x;
		this.y = y;
		this.like = like;
		this.empty = empty;
	}
	
	@Override
	public int compareTo(Student o) {
		if (this.like == o.like) {
			if (this.empty == o.empty) {
				if (this.x == o.x) {
					return this.y - o.y;
				}
				
				return this.x - o.x;
			}
			
			return o.empty - this.empty;
		}
		
		return o.like - this.like;
	}
}

public class Main {
	
	static int[] dx = {-1, 1, 0, 0};
	static int[] dy = {0, 0, -1, 1};
	
	static int n;
	
	static int[][] map;
	static int[] order; // 놀이기구 탑승 순서
	static ArrayList<Integer>[] likeList; // 특정 학생의 좋아하는 학생 번호 리스트

	public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        n = Integer.parseInt(br.readLine());
        
        map = new int[n + 1][n + 1]; // 학생의 번호가 1부터 시작하니까 배열 크기 한 칸 크게함
        order = new int[n * n + 1];
        likeList = new ArrayList[n * n + 1];
        
        for (int i = 1; i <= n * n; i++) {
        	likeList[i] = new ArrayList<>();
        }
        
        for (int i = 1; i <= n * n; i++) {
        	StringTokenizer st = new StringTokenizer(br.readLine());
        	
        	order[i] = Integer.parseInt(st.nextToken()); // 학생 순서 입력
        	
        	for (int j = 0; j < 4; j++) {
        		likeList[order[i]].add(Integer.parseInt(st.nextToken())); // 좋아하는 학생 수 추가
        	}
        }
        
        for (int i = 1; i <= n * n; i++) {
        	ride(order[i]);
        }
        
        System.out.println(getScore());
	}

	// 놀이기구 탑승
	private static void ride(int num) {
		PriorityQueue<Student> pq = new PriorityQueue<>();
		
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= n; j++) {
				// 이미 다른 학생이 탑승되어 있는 경우에는 다음 칸 탐색
				if (map[i][j] > 0) {
					continue;
				}
				
				int like = 0;
				int empty = 0;
				
				// 상하좌우 탐색
				for (int d = 0; d < 4; d++) {
					int nx = i + dx[d];
					int ny = j + dy[d];
					
					if (isRange(nx, ny)) {
						// 인접한 칸에 좋아하는 학생이 있는지 확인
						for (int likeNum : likeList[num]) {
							if (map[nx][ny] == likeNum) {
								like++;
							}
						}
						
						// 인접한 칸중에서 비어있는 칸 확인
						if (map[nx][ny] == 0) {
							empty++;
						}
					}
				}
				
				pq.add(new Student(i, j, like, empty));
			}
		}
		
		// 놀이기구 탑승시킴
		Student cur = pq.poll();
		map[cur.x][cur.y] = num;
	}
	
	// 최종 점수 구하기
	private static int getScore() {
		int result = 0;
		
		for (int i = 1; i <=n; i++) {
			for (int j = 1; j <= n; j++) {
				int num = map[i][j];
				int like = 0;
				
				// 상하좌우 탐색
				for (int d = 0; d < 4; d++) {
					int nx = i + dx[d];
					int ny = j + dy[d];
					
					if (isRange(nx, ny)) {
						// 인접한 칸에 좋아하는 학생이 있는지 확인
						for (int likeNum : likeList[num]) {
							if (map[nx][ny] == likeNum) {
								like++;
							}
						}
					}
				}
				
				switch (like) {
				case 1:
					result += 1;
					break;
					
				case 2:
					result += 10;
					break;
					
				case 3:
					result += 100;
					break;
					
				case 4:
					result += 1000;
					break;
				}
			}
		}
		
		return result;
	}
	
	private static boolean isRange(int x, int y) {
		return x >= 1 && x <= n && y >= 1 && y <= n;
	}
}
