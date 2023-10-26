/**
 * 토끼와 경주
 * 삼성 SW 역량테스트 2023 상반기 오전 2번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/rabit-and-race/description?page=1&pageSize=20
 * 
 * @author minchae
 * @date 2023. 10. 26.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.PriorityQueue;
import java.util.StringTokenizer;
import java.util.HashMap;

class Rabbit implements Comparable<Rabbit> {
	int x;
	int y;
	int j; // 고유번호
	int id; // 점프 횟수
	
	public Rabbit(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Rabbit(int x, int y, int j, int id) {
		this.x = x;
		this.y = y;
		this.j = j;
		this.id = id;
	}

	@Override
	public int compareTo(Rabbit o) {
		if (this.j != o.j) {
			return this.j - o.j;
		}

		if (this.x + this.y != o.x + o.y) {
			return (this.x + this.y) - (o.x + o.y);
		}

		if (this.x != o.x) {
			return this.x - o.x;
		}

		if (this.y != o.y) {
			return this.y - o.y;
		}

		return this.id - o.id;
	}
}

public class Main {
	
	static int N, M, P;
	
	static int[] id; // 토끼의 고유 번호 저장
	static int[] distance; // 토기의 이동거리 저장
	static int[] jumpCnt; // 토끼의 점프 횟수 저장
	static long[] score; // 토끼의 점수 저장
	
	static Rabbit[] pos; // 토끼의 위치 저장
	static boolean[] run; // 각 경주에서 토끼가 달렸는지 확인
	
	static HashMap<Integer, Integer> hm = new HashMap<>(); // 토끼의 고유 번호를 인덱스로 변환
	
	static long totalSum; // 하나를 제외한 모든 토끼의 점수를 더하는 쿼리를 편하게 하기 위해 사용

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		int Q = Integer.parseInt(br.readLine());

		while (Q-- > 0) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			
			int order = Integer.parseInt(st.nextToken());
			
			// 1. 경주 시작 준비
			if (order == 100) {
				N = Integer.parseInt(st.nextToken());
				M = Integer.parseInt(st.nextToken());
				P = Integer.parseInt(st.nextToken()); // 토끼의 수
				
				id = new int[P + 1];
				distance = new int[P + 1];
				jumpCnt = new int[P + 1];
				score = new long[P + 1];
				pos = new Rabbit[P + 1];
				
				for (int i = 1; i <= P; i++) {
					id[i] = Integer.parseInt(st.nextToken());
					distance[i] = Integer.parseInt(st.nextToken());
					hm.put(id[i], i);
					pos[i] = new Rabbit(1, 1); // 모든 토끼는 (1, 1)에서 시작
				}
			}
			
			// 2. 경주 진행
			if (order == 200) {
				int K = Integer.parseInt(st.nextToken()); // 반복 횟수
				int S = Integer.parseInt(st.nextToken()); // 더해줄 점수
				
				start(K, S);
			}
			
			// 3. 이동거리 변경
			if (order == 300) {
				int id = Integer.parseInt(st.nextToken());
				int L = Integer.parseInt(st.nextToken());
				
				int idx = hm.get(id);
				
				distance[idx] *= L; // id인 고유번호를 가진 토끼의 이동거리를 L배 증가시킴
			}
			
			// 4. 최고의 토끼 선정
			if (order == 400) {
				getBestRabit();
			}
		}

	}
	
	// 경주 진행 함수
	private static void start(int K, int S) {
		run = new boolean[P + 1]; // 매 경주마다 초기화 해줘야함
		
		// 일단 토끼들을 다 우선순위큐에 넣음
		PriorityQueue<Rabbit> pq = new PriorityQueue<>();
		
		for (int i = 1; i <= P; i++) {
			pq.add(new Rabbit(pos[i].x, pos[i].y, jumpCnt[i], id[i]));
		}
		
		// K번동안 진행
		while (K-- > 0) {
			Rabbit cur = pq.poll(); // 우선순위가 가장 높은 토끼를 가져옴
			
			// 상하좌우 네 방향으로 이동해봄
			int dis = distance[hm.get(cur.id)]; // 거리
			
			Rabbit next = new Rabbit(0, 0, cur.id, cur.j);
			
			// 토끼를 위로 이동시킴
            Rabbit up = getUpRabbit(new Rabbit(cur.x, cur.y, cur.j, cur.id), dis);
            
            // 지금까지의 도착지들보다 더 멀리 갈 수 있다면 도착지를 갱신
            if (cmp(next, up)) {
            	next = up;
            }
    
            // 토끼를 아래로 이동시킴
            Rabbit down = getDownRabbit(new Rabbit(cur.x, cur.y, cur.j, cur.id), dis);
            
            // 지금까지의 도착지들보다 더 멀리 갈 수 있다면 도착지를 갱신
            if (cmp(next, down)) {
            	next = down;
            }
    
    
            // 토끼를 왼쪽으로 이동시킴
            Rabbit left = getLeftRabbit(new Rabbit(cur.x, cur.y, cur.j, cur.id), dis);
            
            // 지금까지의 도착지들보다 더 멀리 갈 수 있다면 도착지를 갱신
            if (cmp(next, left)) {
            	next = left;
            }
    
    
            // 토끼를 오른쪽으로 이동시킴
            Rabbit right = getRightRabbit(new Rabbit(cur.x, cur.y, cur.j, cur.id), dis);
            
            // 지금까지의 도착지들보다 더 멀리 갈 수 있다면 도착지를 갱신
            if (cmp(next, right)) {
            	next = right;
            }
            
            next.j++; // 점수횟수 증가
            pq.add(next);
            
            int nIdx = hm.get(next.id);
            
            pos[nIdx] = new Rabbit(next.x, next.y); // 위치 변경
            jumpCnt[nIdx]++; // 점프횟수 증가
            
            run[nIdx] = true; // 달렸다는 표시를 해줌
            
            score[nIdx] -= (next.x + next.y); // 달린 토끼를 제외한 나머지 토끼가 (r + c)만큼 점수를 얻기 때문에 해당 토끼한테서 값을 빼주면 됨
            totalSum += (next.x + next.y);
		}
		
        // 보너스 점수를 받을 토끼를 찾음 (이번 경주때 달린 토끼 중 가장 멀리있는 토끼를 찾음)
        Rabbit bonus = new Rabbit(0, 0, 0, 0);
        
        while (!pq.isEmpty()) {
            Rabbit cur = pq.poll();
    
            // 달리지 않은 토끼는 스킵
            if (!run[hm.get(cur.id)]) {
            	continue;
            }
    
            if (cmp(bonus, cur)) {
            	bonus = cur;
            }
        }
    
        // 해당 토끼에게 bonus만큼 점수를 추가
        score[hm.get(bonus.id)] += S;
	}
	
	// 최고의 토끼 선정하는 함수
	private static void getBestRabit() {
		long result = 0;
		
		for (int i = 1; i <= P; i++) {
			result = Math.max(result, score[i] + totalSum);
		}
		
		System.out.println(result); // 최고의 토끼 점수 출력
	}
	
	// 토끼를 위로 이동시킴
	private static Rabbit getUpRabbit(Rabbit cur, int dis) {
		Rabbit up = cur;
		dis %= 2 * (N - 1);

		if (dis >= up.x - 1) {
			dis -= (up.x - 1);
			up.x = 1;
		} else {
			up.x -= dis;
			dis = 0;
		}

		if (dis >= N - up.x) {
			dis -= (N - up.x);
			up.x = N;
		} else {
			up.x += dis;
			dis = 0;
		}

		up.x -= dis;

		return up;
	}

	// 토끼를 아래로 이동시킴
	private static Rabbit getDownRabbit(Rabbit cur, int dis) {
		Rabbit down = cur;
		dis %= 2 * (N - 1);

		if (dis >= N - down.x) {
			dis -= (N - down.x);
			down.x = N;
		} else {
			down.x += dis;
			dis = 0;
		}

		if (dis >= down.x - 1) {
			dis -= (down.x - 1);
			down.x = 1;
		} else {
			down.x -= dis;
			dis = 0;
		}

		down.x += dis;

		return down;
	}

	// 토끼를 왼쪽으로 이동시킴
	private static Rabbit getLeftRabbit(Rabbit cur, int dis) {
		Rabbit left = cur;
		dis %= 2 * (M - 1);

		if (dis >= left.y - 1) {
			dis -= (left.y - 1);
			left.y = 1;
		} else {
			left.y -= dis;
			dis = 0;
		}

		if (dis >= M - left.y) {
			dis -= (M - left.y);
			left.y = M;
		} else {
			left.y += dis;
			dis = 0;
		}

		left.y -= dis;

		return left;
	}

	// 토끼를 오른쪽으로 이동시킴
	private static Rabbit getRightRabbit(Rabbit cur, int dis) {
		Rabbit right = cur;
		dis %= 2 * (M - 1);

		if (dis >= M - right.y) {
			dis -= (M - right.y);
			right.y = M;
		} else {
			right.y += dis;
			dis = 0;
		}

		if (dis >= right.y - 1) {
			dis -= (right.y - 1);
			right.y = 1;
		} else {
			right.y -= dis;
			dis = 0;
		}

		right.y += dis;

		return right;
	}
	
	// 가장 긴 위치를 판단하기 위해 정렬함수를 하나 더 만들어줌
    public static boolean cmp(Rabbit a, Rabbit b) {
        if (a.x + a.y != b.x + b.y) {
        	return a.x + a.y < b.x + b.y;
        }
        
        if (a.x != b.x) {
        	return a.x < b.x;
        }
        
        if (a.y != b.y) {
        	return a.y < b.y;
        }
        
        return a.id < b.id;
    }

}
