package com.worshipsearcher.services;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateListener {

    private GregorianCalendar gregorianCalendar;
    private Date date;
    private SimpleDateFormat simpleDateFormat;

    public DateListener() {
        simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        this.date = new Date(System.currentTimeMillis());
        this.gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(this.date);
    }

    public int getDay() {
        String dateString = simpleDateFormat.format(this.date);
        int begin = dateString.indexOf(".");
        String dayString = dateString.substring(begin + 1, dateString.length());
        begin = dayString.indexOf(".");
        dayString = dayString.substring(begin + 1, dayString.length());
        int day = Integer.parseInt(dayString);
        return day;
    }

    public int getMonth() {
        String dateString = simpleDateFormat.format(this.date);
        int begin = dateString.indexOf(".");
        String monthString = dateString.substring(begin + 1, dateString.length());
        int end = monthString.indexOf(".");
        monthString = monthString.substring(0, end);
        int month = Integer.parseInt(monthString);
        return month;
    }

    public int getYear() {
        String dateString = simpleDateFormat.format(this.date);
        int end = dateString.indexOf(".");
        dateString = dateString.substring(0, end);
        int year = Integer.parseInt(dateString);
        return year;
    }

    private boolean getWeekDay(int dayId) {
        return (this.gregorianCalendar.get(Calendar.DAY_OF_WEEK) == dayId);
    }

    private void incrementDay() {
        this.date.setTime(this.date.getTime() + 86400000);
        this.gregorianCalendar.setTime(this.date);
    }

    private void incrementWeek() {
        this.date.setTime(this.date.getTime() + 86400000 * 7);
        this.gregorianCalendar.setTime(this.date);
    }

    public String searchForGoodDay(int weekid, int dayId) {
        resetDate();
        if (getWeekDay(dayId)) {
            return simpleDateFormat.format(date);
        } else {
            while (!getWeekDay(dayId)) {
                incrementDay();
                Log.d("sh", "day " + getDay());
            }
            int month = getMonth();
            int day = getDay();
            while (!isGoodDay(month, day, weekid)) {
                incrementWeek();
                month = getMonth();
                day = getDay();
                Log.d("sh", "day " + getDay());
            }
            return simpleDateFormat.format(date);
        }
    }

    private void resetDate(){
        this.date = new Date(System.currentTimeMillis());
        gregorianCalendar.setTime(this.date);
    }

    public boolean isGoodDay(int month, int day, int weekid) {
        //Log.d("sh", "weekid " + weekid + " benn");
        Log.d("sh","month " + month + " day " + day + " weekid " + weekid);
        switch (weekid) {

            case 1: {
                Log.d("sh", "weekid egy " + weekid);
                switch (month) {
                    case 1:
                        if (day > 31) {
                            return false;
                        } else {
                            return true;
                        }

                    case 2:
                        if (day > 29) {
                            return false;
                        } else {
                            return true;
                        }

                    case 3:
                        if (day > 31) {
                            return false;
                        } else {
                            return true;
                        }

                    case 4:
                        if (day > 30) {
                            return false;
                        } else {
                            return true;
                        }

                    case 5:
                        if (day > 31) {
                            return false;
                        } else {
                            return true;
                        }

                    case 6:
                        if (day > 30) {
                            return false;
                        } else {
                            return true;
                        }

                    case 7:
                        Log.d("sh","7. hónap");
                        if (day > 31) {
                            //Log.d("sh","7. hónap false");
                            return false;
                        } else {
                            //Log.d("sh","7. hónap true");
                            return true;
                        }

                    case 8:
                        if (day > 31) {
                            return false;
                        } else {
                            return true;
                        }

                    case 9:
                        if (day > 30) {
                            return false;
                        } else {
                            return true;
                        }

                    case 10:
                        if (day > 31) {
                            return false;
                        }

                    case 11:
                        if (day > 30) {
                            return false;
                        } else {
                            return true;
                        }

                    case 12:
                        if (day > 31) {
                            return false;
                        } else {
                            return true;
                        }
                }
                return true;
            }
            case 2: {
                if (day >= 1 && day <= 7) {
                    return true;
                } else {
                    return false;
                }

            }
            case 3: {
                if (day >= 8 && day <= 14) {
                    return true;
                } else {
                    return false;
                }

            }
            case 4: {
                if (day >= 15 && day <= 21) {
                    return true;
                } else {
                    return false;
                }

            }
            case 5: {
                if (day >= 22 && day <= 28) {
                    return true;
                } else {
                    return false;
                }

            }
            case 14: {
                switch (month) {
                    case 1:
                        if (day >= 29 && day <= 31) {
                            return true;
                        } else {
                            return false;
                        }

                    case 2:
                        if (day == 29) {
                            return true;
                        } else {
                            return false;
                        }

                    case 3:
                        if (day >= 29 && day <= 31) {
                            return true;
                        } else {
                            return false;
                        }

                    case 4:
                        if (day >= 29 && day <= 30) {
                            return true;
                        } else {
                            return false;
                        }

                    case 5:
                        if (day >= 29 && day <= 31) {
                            return true;
                        } else {
                            return false;
                        }

                    case 6:
                        if (day >= 29 && day <= 30) {
                            return true;
                        } else {
                            return false;
                        }

                    case 7:
                        if (day >= 29 && day <= 31) {
                            return true;
                        } else {
                            return false;
                        }

                    case 8:
                        if (day >= 29 && day <= 31) {
                            return true;
                        } else {
                            return false;
                        }

                    case 9:
                        if (day >= 29 && day <= 30) {
                            return true;
                        } else {
                            return false;
                        }

                    case 10:
                        if (day >= 29 && day <= 31) {
                            return true;
                        } else {
                            return false;
                        }

                    case 11:
                        if (day >= 29 && day <= 30) {
                            return true;
                        } else {
                            return false;
                        }

                    case 12:
                        if (day >= 29 && day <= 31) {
                            return true;
                        } else {
                            return false;
                        }

                }
            }

            case 6: {
                if ((day >= 8 && day <= 14) || (day >= 22 && day <= 28)) {
                    return true;
                } else {
                    return false;
                }
            }
            case 7: {
                if ((day >= 1 && day <= 7) || (day >= 15 && day <= 21)
                        || (day >= 29)) {
                    return true;
                } else {
                    return false;
                }
            }
            case 10: {
                if ((day >= 1 && day <= 7) || (day >= 15 && day <= 21)) {
                    return true;
                } else {
                    return false;
                }
            }
            case 15: {
                switch (month) {
                    case 1:
                        if (day >= 25 && day <= 31) {
                            return true;
                        } else {
                            return false;
                        }
                    case 2:
                        if (day >= 23 && day <= 29) {
                            return true;
                        } else {
                            return false;
                        }
                    case 3:
                        if (day >= 25 && day <= 31) {
                            return true;
                        } else {
                            return false;
                        }
                    case 4:
                        if (day >= 24 && day <= 31) {
                            return true;
                        } else {
                            return false;
                        }
                    case 5:
                        if (day >= 25 && day <= 31) {
                            return true;
                        } else {
                            return false;
                        }
                    case 6:
                        if (day >= 24 && day <= 31) {
                            return true;
                        } else {
                            return false;
                        }
                    case 7:
                        if (day >= 25 && day <= 31) {
                            return true;
                        } else {
                            return false;
                        }
                    case 8:
                        if (day >= 25 && day <= 31) {
                            return true;
                        } else {
                            return false;
                        }
                    case 9:
                        if (day >= 24 && day <= 31) {
                            return true;
                        } else {
                            return false;
                        }
                    case 10:
                        if (day >= 25 && day <= 31) {
                            return true;
                        } else {
                            return false;
                        }
                    case 11:
                        if (day >= 24 && day <= 31) {
                            return true;
                        } else {
                            return false;
                        }
                    case 12:
                        if (day >= 25 && day <= 31) {
                            return true;
                        } else {
                            return false;
                        }
                }

            }
            default:
                //Log.d("sh","default false");
                return false;
        }

    }
}
