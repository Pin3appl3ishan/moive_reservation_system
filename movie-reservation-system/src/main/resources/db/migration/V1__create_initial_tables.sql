CREATE TABLE users (
  id BIGSERIAL PRIMARY KEY,
  username VARCHAR(100) UNIQUE NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  role VARCHAR(20) NOT NULL DEFAULT 'ROLE_USER',
  created_at timestamptz DEFAULT now()
);

CREATE TABLE movies (
  id BIGSERIAL PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  description TEXT,
  poster_url TEXT,
  duration_minutes INT,
  genre VARCHAR(100),
  created_at timestamptz DEFAULT now()
);

CREATE TABLE theaters (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  address TEXT
);

CREATE TABLE screens (
  id BIGSERIAL PRIMARY KEY,
  theater_id BIGINT REFERENCES theaters(id) ON DELETE CASCADE,
  name VARCHAR(100),
  capacity INT NOT NULL
);

CREATE TABLE seats (
  id BIGSERIAL PRIMARY KEY,
  screen_id BIGINT REFERENCES screens(id) ON DELETE CASCADE,
  label VARCHAR(16) NOT NULL,
  row_label VARCHAR(8),
  col INT,
  UNIQUE(screen_id, label)
);

CREATE TABLE showtimes (
  id BIGSERIAL PRIMARY KEY,
  movie_id BIGINT REFERENCES movies(id) ON DELETE CASCADE,
  screen_id BIGINT REFERENCES screens(id) ON DELETE CASCADE,
  start_time timestamptz NOT NULL,
  end_time timestamptz NOT NULL,
  ticket_price NUMERIC(10,2) NOT NULL,
  created_at timestamptz DEFAULT now()
);

CREATE TABLE reservations (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
  showtime_id BIGINT REFERENCES showtimes(id) ON DELETE CASCADE,
  total_amount NUMERIC(10,2) NOT NULL DEFAULT 0,
  status VARCHAR(20) NOT NULL, -- e.g., 'HELD', 'CONFIRMED', 'CANCELLED'
  hold_expiry timestamptz, -- For temporary holds
  created_at timestamptz DEFAULT now()
);

CREATE TABLE seat_reservations (
  id BIGSERIAL PRIMARY KEY,
  reservation_id BIGINT REFERENCES reservations(id) ON DELETE CASCADE,
  seat_id BIGINT REFERENCES seats(id) ON DELETE CASCADE,
  showtime_id BIGINT REFERENCES showtimes(id) ON DELETE CASCADE,
  status VARCHAR(20) NOT NULL, -- e.g., 'HELD', 'PAID', 'CANCELLED'
  created_at timestamptz DEFAULT now(),
  UNIQUE(showtime_id, seat_id) -- Prevents overbooking
);

-- Performance Indexes
CREATE INDEX idx_showtimes_start_time ON showtimes(start_time);
CREATE INDEX idx_reservations_user_id ON reservations(user_id);
CREATE INDEX idx_reservations_showtime_id ON reservations(showtime_id);
CREATE INDEX idx_seat_reservations_showtime_seat ON seat_reservations(showtime_id, seat_id);
CREATE INDEX idx_seat_reservations_reservation_id ON seat_reservations(reservation_id);
CREATE INDEX idx_seats_screen_id ON seats(screen_id);
CREATE INDEX idx_screens_theater_id ON screens(theater_id);