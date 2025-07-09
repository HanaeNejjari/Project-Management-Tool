FROM node:18 AS build
WORKDIR /app
COPY . .

RUN ls -l /app
RUN npm install
RUN npm run build --prod

RUN ls -l /app/dist
RUN ls -l /app/dist/project-management-tool/browser

FROM nginx:alpine
COPY --from=build /app/dist/project-management-tool/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
