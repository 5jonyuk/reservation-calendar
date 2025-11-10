FROM node:22
WORKDIR /app

# package.json 먼저 복사
COPY package*.json ./

RUN npm install

# 나머지 소스 복사
COPY . .

# Vite dev 서버 실행
CMD ["npm", "run", "dev"]

# 개발용 포트
EXPOSE 5173