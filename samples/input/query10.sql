SELECT DISTINCT Reserves.G,  s.C, s2.A,b0.D, s2.B, b0.E, b0.F, b1.D, b1.E, b1.F,Reserves.H
FROM Reserves,
     Sailors s,
     Sailors s2,
     Boats b0,
     Boats b1
WHERE Reserves.H >= 102
  and 100 = s.B
  and s2.A = 6
  and Reserves.H < 104
  and s.A = Reserves.G
  and s2.A<s2.B
  and b1.D<103
  ORDER BY b0.E,b0.F,b0.D, b1.F